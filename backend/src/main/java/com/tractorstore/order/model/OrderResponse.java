package com.tractorstore.order.model;

import com.tractorstore.cart.model.LineItemDto;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/** Pedido confirmado (página thanks). */
public record OrderResponse(
    String id,
    String customerName,
    String customerEmail,
    String pickupStoreId,
    List<LineItemDto> lines,
    BigDecimal total,
    Instant placedAt) {}
