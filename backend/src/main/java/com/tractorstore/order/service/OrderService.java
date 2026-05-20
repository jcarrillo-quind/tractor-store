package com.tractorstore.order.service;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.cart.entities.Cart;
import com.tractorstore.cart.model.LineItemDto;
import com.tractorstore.cart.service.CartService;
import com.tractorstore.cart.support.CartSessionSupport;
import com.tractorstore.order.entities.Order;
import com.tractorstore.order.model.OrderResponse;
import com.tractorstore.order.model.PlaceOrderRequest;
import com.tractorstore.order.usecases.GetOrderByIdUseCase;
import com.tractorstore.order.usecases.PlaceOrderUseCase;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

  private final PlaceOrderUseCase placeOrder;
  private final GetOrderByIdUseCase getOrder;
  private final CartService cartService;
  private final CartSessionSupport sessionSupport;
  private final StoreReadPort stores;

  public OrderService(
      CartService cartService, CartSessionSupport sessionSupport, SeedBundle seedBundle) {
    this.cartService = cartService;
    this.sessionSupport = sessionSupport;
    this.stores = seedBundle.storeReadPort();
    this.placeOrder = new PlaceOrderUseCase(seedBundle.orderWritePort());
    this.getOrder = new GetOrderByIdUseCase(seedBundle.orderReadPort());
  }

  public OrderResponse placeOrder(
      PlaceOrderRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
    validatePlaceOrderRequest(request);
    String sessionId = sessionSupport.resolveSessionId(httpRequest, httpResponse);
    Cart cart = cartService.cartForSession(sessionId);
    if (cart.lineItems().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
    }
    String storeId = request.pickupStoreId().strip();
    boolean storeExists = stores.allStores().stream().anyMatch(s -> s.id().equals(storeId));
    if (!storeExists) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Tienda de recogida no válida: " + storeId);
    }
    Order order =
        placeOrder.execute(
            request.customerName().strip(), request.customerEmail().strip(), storeId, cart);
    cartService.clearSession(sessionId);
    cart.clear();
    return toResponse(order);
  }

  public Optional<OrderResponse> getOrder(String id) {
    UUID orderId;
    try {
      orderId = UUID.fromString(id);
    } catch (IllegalArgumentException ex) {
      return Optional.empty();
    }
    return getOrder.execute(orderId).map(this::toResponse);
  }

  private static void validatePlaceOrderRequest(PlaceOrderRequest request) {
    if (request == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuerpo requerido");
    }
    if (request.customerName() == null || request.customerName().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customerName requerido");
    }
    if (request.customerEmail() == null || request.customerEmail().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customerEmail requerido");
    }
    if (request.pickupStoreId() == null || request.pickupStoreId().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pickupStoreId requerido");
    }
  }

  private OrderResponse toResponse(Order order) {
    List<LineItemDto> lines =
        order.lines().stream()
            .map(
                line ->
                    new LineItemDto(
                        line.sku(), line.quantity(), line.unitPrice(), line.lineTotal()))
            .toList();
    return new OrderResponse(
        order.id().toString(),
        order.customerName(),
        order.customerEmail(),
        order.pickupStoreId(),
        lines,
        order.total(),
        order.placedAt());
  }
}
