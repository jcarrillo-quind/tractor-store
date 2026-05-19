package com.tractorstore.order.usecases.ports;

import com.tractorstore.order.entities.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderReadPort {

  Optional<Order> findById(UUID id);
}
