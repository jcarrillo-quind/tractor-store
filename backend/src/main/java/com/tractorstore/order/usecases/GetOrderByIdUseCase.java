package com.tractorstore.order.usecases;

import com.tractorstore.order.entities.Order;
import com.tractorstore.order.usecases.ports.OrderReadPort;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class GetOrderByIdUseCase {

  private final OrderReadPort orders;

  public GetOrderByIdUseCase(OrderReadPort orders) {
    this.orders = Objects.requireNonNull(orders, "orders");
  }

  public Optional<Order> execute(UUID id) {
    return orders.findById(id);
  }
}
