package com.tractorstore.order.usecases.ports;

import com.tractorstore.order.entities.Order;

public interface OrderWritePort {

  void save(Order order);
}
