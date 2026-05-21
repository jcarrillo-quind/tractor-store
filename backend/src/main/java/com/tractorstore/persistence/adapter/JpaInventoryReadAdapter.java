package com.tractorstore.persistence.adapter;

import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import com.tractorstore.persistence.repository.InventoryJpaRepository;

/** Adaptador JPA para {@link InventoryReadPort}. */
public final class JpaInventoryReadAdapter implements InventoryReadPort {

  private final InventoryJpaRepository inventory;

  public JpaInventoryReadAdapter(InventoryJpaRepository inventory) {
    this.inventory = inventory;
  }

  @Override
  public int availableUnits(String sku) {
    if (sku == null || sku.isBlank()) {
      return 0;
    }
    return inventory
        .findById(sku.strip())
        .map(row -> Math.max(0, row.getAvailableUnits()))
        .orElse(0);
  }

  @Override
  public boolean inStock(String sku) {
    return availableUnits(sku) > 0;
  }
}
