package com.tractorstore.cart.model;

import java.math.BigDecimal;

/** Línea del carrito (alineado con {@code LineItem} del front). */
public record LineItemDto(
    String sku,
    int quantity,
    BigDecimal unitPrice,
    BigDecimal lineTotal,
    String label,
    String imageUrl) {}
