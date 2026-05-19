package com.tractorstore.catalog.entities;

import java.math.BigDecimal;
import java.util.Objects;

/** Variante vendible (SKU) de un producto del catálogo. */
public record Variant(
    String sku, String label, String colorHex, BigDecimal price, String productId) {
  public Variant {
    Objects.requireNonNull(sku, "sku");
    Objects.requireNonNull(colorHex, "colorHex");
    Objects.requireNonNull(price, "price");
    Objects.requireNonNull(productId, "productId");
    if (sku.isBlank()) {
      throw new IllegalArgumentException("sku no puede estar vacío");
    }
  }
}
