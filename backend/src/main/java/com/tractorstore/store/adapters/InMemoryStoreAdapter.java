package com.tractorstore.store.adapters;

import java.util.List;
import java.util.Objects;

import com.tractorstore.store.entities.Store;
import com.tractorstore.store.usecases.ports.StoreReadPort;

public final class InMemoryStoreAdapter implements StoreReadPort {

	private final List<Store> stores;

	public InMemoryStoreAdapter(List<Store> stores) {
		this.stores = List.copyOf(Objects.requireNonNull(stores, "stores"));
	}

	@Override
	public List<Store> allStores() {
		return stores;
	}
}
