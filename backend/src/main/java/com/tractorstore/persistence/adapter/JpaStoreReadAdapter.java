package com.tractorstore.persistence.adapter;

import com.tractorstore.persistence.mapper.PersistenceMapper;
import com.tractorstore.persistence.repository.StoreJpaRepository;
import com.tractorstore.store.entities.Store;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import java.util.List;

/** Adaptador JPA para {@link StoreReadPort}. */
public final class JpaStoreReadAdapter implements StoreReadPort {

  private final StoreJpaRepository stores;
  private final PersistenceMapper mapper;

  public JpaStoreReadAdapter(StoreJpaRepository stores, PersistenceMapper mapper) {
    this.stores = stores;
    this.mapper = mapper;
  }

  @Override
  public List<Store> allStores() {
    return stores.findAllByOrderByNameAsc().stream().map(mapper::toDomain).toList();
  }
}
