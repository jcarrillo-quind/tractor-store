package com.tractorstore.cart.usecases;

import com.tractorstore.cart.entities.Cart;
import com.tractorstore.cart.entities.LineItem;
import java.util.Objects;

public final class AddOrMergeCartLineUseCase {

  public void execute(Cart cart, LineItem line) {
    Objects.requireNonNull(cart, "cart");
    cart.putItem(line);
  }
}
