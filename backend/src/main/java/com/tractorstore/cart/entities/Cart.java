package com.tractorstore.cart.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Carrito mutable por sesión (agregado de dominio). */
public final class Cart {

  private final Map<String, LineItem> itemsBySku = new LinkedHashMap<>();

  public void clear() {
    itemsBySku.clear();
  }

  public List<LineItem> lineItems() {
    return List.copyOf(itemsBySku.values());
  }

  public Optional<LineItem> lineForSku(String sku) {
    if (sku == null || sku.isBlank()) {
      return Optional.empty();
    }
    return Optional.ofNullable(itemsBySku.get(sku.strip()));
  }

  public void putItem(LineItem line) {
    Objects.requireNonNull(line, "line");
    String key = line.sku();
    LineItem merged = line;
    if (itemsBySku.containsKey(key)) {
      LineItem prev = itemsBySku.get(key);
      int q = prev.quantity() + line.quantity();
      merged = new LineItem(key, q, prev.unitPrice());
    }
    itemsBySku.put(key, merged);
  }

  public boolean removeSku(String sku) {
    if (sku == null || sku.isBlank()) {
      return false;
    }
    return itemsBySku.remove(sku.strip()) != null;
  }

  public int totalItemCount() {
    return itemsBySku.values().stream().mapToInt(LineItem::quantity).sum();
  }

  public BigDecimal subtotal() {
    return itemsBySku.values().stream()
        .map(LineItem::lineTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public List<String> skus() {
    return Collections.unmodifiableList(new ArrayList<>(itemsBySku.keySet()));
  }
}
