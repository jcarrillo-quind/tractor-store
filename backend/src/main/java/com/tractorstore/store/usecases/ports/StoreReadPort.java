package com.tractorstore.store.usecases.ports;

import com.tractorstore.store.entities.Store;
import java.util.List;

public interface StoreReadPort {

  List<Store> allStores();
}
