package com.tractorstore.order.usecases;

import com.tractorstore.cart.entities.Cart;
import com.tractorstore.cart.entities.LineItem;
import com.tractorstore.order.entities.Order;
import com.tractorstore.order.usecases.ports.OrderWritePort;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class PlaceOrderUseCase {

  private final OrderWritePort orders;

  public PlaceOrderUseCase(OrderWritePort orders) {
    this.orders = Objects.requireNonNull(orders, "orders");
  }

  public Order execute(String customerName, String customerEmail, String pickupStoreId, Cart cart) {
    Objects.requireNonNull(cart, "cart");
    List<LineItem> lines = List.copyOf(cart.lineItems());
    BigDecimal total = cart.subtotal();

    Order order =
        new Order(
            UUID.randomUUID(),
            customerName,
            customerEmail,
            pickupStoreId,
            lines,
            total,
            Instant.now());
    orders.save(order);
    return order;
  }
}
