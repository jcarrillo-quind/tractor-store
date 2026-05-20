package com.tractorstore.cart.model;

import java.math.BigDecimal;
import java.util.List;

/** Estado completo del carrito. */
public record CartResponse(
    List<LineItemDto> lines, int itemCount, BigDecimal subtotal, List<String> skus) {}
