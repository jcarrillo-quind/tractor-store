package com.tractorstore.cart.usecases;

import java.util.Objects;

import com.tractorstore.cart.entities.Cart;

public final class RemoveCartLineUseCase {

	public boolean execute(Cart cart, String sku) {
		Objects.requireNonNull(cart, "cart");
		return cart.removeSku(sku);
	}
}
