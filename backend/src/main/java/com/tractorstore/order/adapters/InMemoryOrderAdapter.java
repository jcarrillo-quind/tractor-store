package com.tractorstore.order.adapters;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.tractorstore.order.entities.Order;
import com.tractorstore.order.usecases.ports.OrderReadPort;
import com.tractorstore.order.usecases.ports.OrderWritePort;

/**
 * Almacén de pedidos en memoria (fase 1).
 */
public final class InMemoryOrderAdapter implements OrderReadPort, OrderWritePort {

	private final Map<UUID, Order> orders = new ConcurrentHashMap<>();

	@Override
	public void save(Order order) {
		Objects.requireNonNull(order, "order");
		orders.put(order.id(), order);
	}

	@Override
	public Optional<Order> findById(UUID id) {
		if (id == null) {
			return Optional.empty();
		}
		return Optional.ofNullable(orders.get(id));
	}
}
