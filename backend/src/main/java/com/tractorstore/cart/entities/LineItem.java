package com.tractorstore.cart.entities;

import java.math.BigDecimal;
import java.util.Objects;

/** Línea del carrito (snapshot de precio al añadir). */
public record LineItem(String sku, int quantity, BigDecimal unitPrice) {
  public LineItem {
    Objects.requireNonNull(sku, "sku");
    Objects.requireNonNull(unitPrice, "unitPrice");
    if (sku.isBlank()) {
      throw new IllegalArgumentException("sku requerido");
    }
    if (quantity < 1) {
      throw new IllegalArgumentException("quantity >= 1");
    }
    if (unitPrice.signum() < 0) {
      throw new IllegalArgumentException("unitPrice negativo");
    }
  }

  public BigDecimal lineTotal() {
    return unitPrice.multiply(BigDecimal.valueOf(quantity));
  }
}
