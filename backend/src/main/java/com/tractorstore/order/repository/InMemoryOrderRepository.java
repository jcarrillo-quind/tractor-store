package com.tractorstore.order.repository;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.order.entities.Order;
import com.tractorstore.order.usecases.ports.OrderReadPort;
import com.tractorstore.order.usecases.ports.OrderWritePort;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryOrderRepository {

  private final OrderWritePort write;
  private final OrderReadPort read;

  public InMemoryOrderRepository(SeedBundle seedBundle) {
    this.write = seedBundle.orderWritePort();
    this.read = seedBundle.orderReadPort();
  }

  public Order save(Order order) {
    write.save(order);
    return order;
  }

  public Optional<Order> findById(UUID id) {
    return read.findById(id);
  }
}
