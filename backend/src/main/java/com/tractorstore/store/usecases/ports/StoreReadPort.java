package com.tractorstore.store.usecases.ports;

import java.util.List;

import com.tractorstore.store.entities.Store;

public interface StoreReadPort {

	List<Store> allStores();
}
