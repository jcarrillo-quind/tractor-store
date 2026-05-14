package com.tractorstore.cart.usecases;

import java.util.Objects;

import com.tractorstore.cart.entities.Cart;
import com.tractorstore.cart.entities.LineItem;

public final class AddOrMergeCartLineUseCase {

	public void execute(Cart cart, LineItem line) {
		Objects.requireNonNull(cart, "cart");
		cart.putItem(line);
	}
}
