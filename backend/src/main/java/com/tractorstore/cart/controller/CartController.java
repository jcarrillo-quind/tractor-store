package com.tractorstore.cart.controller;

import com.tractorstore.cart.model.AddCartItemRequest;
import com.tractorstore.cart.model.CartResponse;
import com.tractorstore.cart.model.MiniCartResponse;
import com.tractorstore.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {

  private final CartService cartService;

  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @GetMapping
  public CartResponse cart(HttpServletRequest request, HttpServletResponse response) {
    return cartService.getCart(request, response);
  }

  @GetMapping("/mini")
  public MiniCartResponse miniCart(HttpServletRequest request, HttpServletResponse response) {
    return cartService.getMiniCart(request, response);
  }

  @PostMapping("/items")
  public CartResponse addItem(
      @RequestBody AddCartItemRequest body,
      HttpServletRequest request,
      HttpServletResponse response) {
    return cartService.addItem(body, request, response);
  }

  @DeleteMapping("/items/{sku}")
  public CartResponse removeItem(
      @PathVariable String sku, HttpServletRequest request, HttpServletResponse response) {
    return cartService.removeItem(sku, request, response);
  }
}
