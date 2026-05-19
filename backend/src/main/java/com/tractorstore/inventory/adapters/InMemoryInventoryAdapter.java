package com.tractorstore.inventory.adapters;

import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import java.util.Map;
import java.util.Objects;

/** Stock en memoria (dataset inicial / sin integración externa). */
public final class InMemoryInventoryAdapter implements InventoryReadPort {

  private final Map<String, Integer> stockBySku;

  public InMemoryInventoryAdapter(Map<String, Integer> stockBySku) {
    this.stockBySku = Map.copyOf(Objects.requireNonNull(stockBySku, "stockBySku"));
  }

  @Override
  public int availableUnits(String sku) {
    if (sku == null || sku.isBlank()) {
      return 0;
    }
    return Math.max(0, stockBySku.getOrDefault(sku.strip(), 0));
  }

  @Override
  public boolean inStock(String sku) {
    return availableUnits(sku) > 0;
  }
}
