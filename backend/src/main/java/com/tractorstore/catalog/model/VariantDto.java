package com.tractorstore.catalog.model;

import java.math.BigDecimal;

/** Variante vendible (alineado con el modelo TypeScript {@code Variant}). */
public record VariantDto(
    String sku, String label, String colorHex, BigDecimal price, String productId) {}
