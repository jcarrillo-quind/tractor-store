package com.tractorstore.inventory.repository;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryInventoryRepository {

  private final InventoryReadPort inventory;

  public InMemoryInventoryRepository(SeedBundle seedBundle) {
    this.inventory = seedBundle.inventoryReadPort();
  }

  public int availableUnits(String sku) {
    return inventory.availableUnits(sku);
  }

  public boolean inStock(String sku) {
    return inventory.inStock(sku);
  }
}
