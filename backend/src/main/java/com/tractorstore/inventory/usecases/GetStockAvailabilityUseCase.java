package com.tractorstore.inventory.usecases;

import java.util.Objects;

import com.tractorstore.inventory.usecases.ports.InventoryReadPort;

public final class GetStockAvailabilityUseCase {

	private final InventoryReadPort inventory;

	public GetStockAvailabilityUseCase(InventoryReadPort inventory) {
		this.inventory = Objects.requireNonNull(inventory, "inventory");
	}

	public int availableUnits(String sku) {
		return inventory.availableUnits(sku);
	}

	public boolean inStock(String sku) {
		return inventory.inStock(sku);
	}
}
