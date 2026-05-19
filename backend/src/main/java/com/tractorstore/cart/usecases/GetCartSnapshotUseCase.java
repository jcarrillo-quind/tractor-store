package com.tractorstore.cart.usecases;

import com.tractorstore.cart.entities.Cart;
import java.util.Objects;

public final class GetCartSnapshotUseCase {

  public CartSnapshot execute(Cart cart) {
    Objects.requireNonNull(cart, "cart");
    return new CartSnapshot(cart.lineItems(), cart.totalItemCount(), cart.subtotal());
  }
}
