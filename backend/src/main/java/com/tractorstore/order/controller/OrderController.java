package com.tractorstore.order.controller;

import com.tractorstore.order.model.OrderResponse;
import com.tractorstore.order.model.PlaceOrderRequest;
import com.tractorstore.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public OrderResponse placeOrder(
      @RequestBody PlaceOrderRequest body,
      HttpServletRequest request,
      HttpServletResponse response) {
    return orderService.placeOrder(body, request, response);
  }

  @GetMapping("/{id}")
  public OrderResponse order(@PathVariable String id) {
    return orderService
        .getOrder(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado: " + id));
  }
}
