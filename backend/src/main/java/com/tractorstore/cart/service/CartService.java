package com.tractorstore.cart.service;

import com.tractorstore.cart.entities.Cart;
import com.tractorstore.cart.entities.LineItem;
import com.tractorstore.cart.model.AddCartItemRequest;
import com.tractorstore.cart.model.CartResponse;
import com.tractorstore.cart.model.LineItemDto;
import com.tractorstore.cart.model.MiniCartResponse;
import com.tractorstore.cart.repository.InMemoryCartSessionRepository;
import com.tractorstore.cart.support.CartSessionSupport;
import com.tractorstore.cart.usecases.AddOrMergeCartLineUseCase;
import com.tractorstore.cart.usecases.CartSnapshot;
import com.tractorstore.cart.usecases.GetCartSnapshotUseCase;
import com.tractorstore.cart.usecases.RemoveCartLineUseCase;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.usecases.FindVariantBySkuUseCase;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.inventory.usecases.GetStockAvailabilityUseCase;
import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CartService {

  private final InMemoryCartSessionRepository sessions;
  private final CartSessionSupport sessionSupport;
  private final GetCartSnapshotUseCase snapshotUseCase;
  private final AddOrMergeCartLineUseCase addLineUseCase;
  private final RemoveCartLineUseCase removeLineUseCase;
  private final FindVariantBySkuUseCase findVariant;
  private final GetStockAvailabilityUseCase stock;

  public CartService(
      InMemoryCartSessionRepository sessions,
      CartSessionSupport sessionSupport,
      CatalogReadPort catalog,
      InventoryReadPort inventory) {
    this.sessions = sessions;
    this.sessionSupport = sessionSupport;
    this.snapshotUseCase = new GetCartSnapshotUseCase();
    this.addLineUseCase = new AddOrMergeCartLineUseCase();
    this.removeLineUseCase = new RemoveCartLineUseCase();
    this.findVariant = new FindVariantBySkuUseCase(catalog);
    this.stock = new GetStockAvailabilityUseCase(inventory);
  }

  public CartResponse getCart(HttpServletRequest request, HttpServletResponse response) {
    return toResponse(cartForRequest(request, response));
  }

  public MiniCartResponse getMiniCart(HttpServletRequest request, HttpServletResponse response) {
    CartSnapshot snapshot = snapshotUseCase.execute(cartForRequest(request, response));
    return new MiniCartResponse(snapshot.itemCount());
  }

  public CartResponse addItem(
      AddCartItemRequest body, HttpServletRequest request, HttpServletResponse response) {
    if (body == null || body.sku() == null || body.sku().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sku requerido");
    }
    String sku = body.sku().strip();
    Variant variant =
        findVariant
            .execute(sku)
            .orElseThrow(
                () ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "SKU no encontrado: " + sku));
    if (!stock.inStock(sku)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "SKU sin stock: " + sku);
    }
    Cart cart = cartForRequest(request, response);
    addLineUseCase.execute(cart, new LineItem(sku, 1, variant.price()));
    return toResponse(cart);
  }

  public CartResponse removeItem(
      String sku, HttpServletRequest request, HttpServletResponse response) {
    if (sku == null || sku.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sku requerido");
    }
    Cart cart = cartForRequest(request, response);
    boolean removed = removeLineUseCase.execute(cart, sku);
    if (!removed) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Línea no encontrada: " + sku);
    }
    return toResponse(cart);
  }

  public Cart cartForSession(String sessionId) {
    return sessions.getOrCreate(sessionId);
  }

  public void clearSession(String sessionId) {
    sessions.clear(sessionId);
  }

  private Cart cartForRequest(HttpServletRequest request, HttpServletResponse response) {
    String sessionId = sessionSupport.resolveSessionId(request, response);
    return sessions.getOrCreate(sessionId);
  }

  private CartResponse toResponse(Cart cart) {
    CartSnapshot snapshot = snapshotUseCase.execute(cart);
    List<LineItemDto> lines =
        snapshot.lines().stream()
            .map(
                line -> {
                  Variant variant =
                      findVariant
                          .execute(line.sku())
                          .orElseThrow(
                              () ->
                                  new IllegalStateException(
                                      "SKU en carrito sin catálogo: " + line.sku()));
                  return new LineItemDto(
                      line.sku(),
                      line.quantity(),
                      line.unitPrice(),
                      line.lineTotal(),
                      variant.label(),
                      variant.imageUrl());
                })
            .toList();
    return new CartResponse(
        lines, snapshot.itemCount(), snapshot.subtotal(), List.copyOf(cart.skus()));
  }
}
