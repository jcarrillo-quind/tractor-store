package com.tractorstore.order.model;

/** Cuerpo de POST /api/orders. */
public record PlaceOrderRequest(String customerName, String customerEmail, String pickupStoreId) {}
