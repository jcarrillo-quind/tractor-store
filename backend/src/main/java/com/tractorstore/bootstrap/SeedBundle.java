package com.tractorstore.bootstrap;

import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import com.tractorstore.order.usecases.ports.OrderReadPort;
import com.tractorstore.order.usecases.ports.OrderWritePort;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import java.util.Objects;

/** Raíz de composición: puertos de los módulos cableados por adaptadores de infraestructura. */
public record SeedBundle(
    CatalogReadPort catalogReadPort,
    StoreReadPort storeReadPort,
    InventoryReadPort inventoryReadPort,
    OrderWritePort orderWritePort,
    OrderReadPort orderReadPort) {

  public SeedBundle {
    Objects.requireNonNull(catalogReadPort, "catalogReadPort");
    Objects.requireNonNull(storeReadPort, "storeReadPort");
    Objects.requireNonNull(inventoryReadPort, "inventoryReadPort");
    Objects.requireNonNull(orderWritePort, "orderWritePort");
    Objects.requireNonNull(orderReadPort, "orderReadPort");
  }
}
