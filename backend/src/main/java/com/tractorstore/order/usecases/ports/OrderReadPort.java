package com.tractorstore.order.usecases.ports;

import java.util.Optional;
import java.util.UUID;

import com.tractorstore.order.entities.Order;

public interface OrderReadPort {

	Optional<Order> findById(UUID id);
}
