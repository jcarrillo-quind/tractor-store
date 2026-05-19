package com.tractorstore.cart.usecases;

import com.tractorstore.cart.entities.Cart;
import java.util.Objects;

public final class RemoveCartLineUseCase {

  public boolean execute(Cart cart, String sku) {
    Objects.requireNonNull(cart, "cart");
    return cart.removeSku(sku);
  }
}
