package com.tractorstore.order.entities;

import com.tractorstore.cart.entities.LineItem;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Pedido confirmado (thanks page / historización). */
public record Order(
    UUID id,
    String customerName,
    String customerEmail,
    String pickupStoreId,
    List<LineItem> lines,
    BigDecimal total,
    Instant placedAt) {

  public Order {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(customerName, "customerName");
    Objects.requireNonNull(customerEmail, "customerEmail");
    Objects.requireNonNull(pickupStoreId, "pickupStoreId");
    Objects.requireNonNull(total, "total");
    Objects.requireNonNull(placedAt, "placedAt");
    if (customerName.isBlank()) {
      throw new IllegalArgumentException("customerName requerido");
    }
    if (customerEmail.isBlank()) {
      throw new IllegalArgumentException("customerEmail requerido");
    }
    if (pickupStoreId.isBlank()) {
      throw new IllegalArgumentException("pickupStoreId requerido");
    }
    lines = lines == null ? List.of() : List.copyOf(lines);
  }
}
