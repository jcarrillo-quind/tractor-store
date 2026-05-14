package com.tractorstore.store.usecases;

import java.util.List;
import java.util.Objects;

import com.tractorstore.store.entities.Store;
import com.tractorstore.store.usecases.ports.StoreReadPort;

public final class ListStoresUseCase {

	private final StoreReadPort stores;

	public ListStoresUseCase(StoreReadPort stores) {
		this.stores = Objects.requireNonNull(stores, "stores");
	}

	public List<Store> execute() {
		return List.copyOf(stores.allStores());
	}
}
