package com.tractorstore.config;

import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import java.util.Objects;

/** Producto de la fábrica de persistencia: puertos de lectura cableados al modo activo. */
public record PersistencePorts(
    CatalogReadPort catalog, StoreReadPort store, InventoryReadPort inventory) {

  public PersistencePorts {
    Objects.requireNonNull(catalog, "catalog");
    Objects.requireNonNull(store, "store");
    Objects.requireNonNull(inventory, "inventory");
  }
}
