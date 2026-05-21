package com.tractorstore.inventory.service;

import com.tractorstore.inventory.model.StockAvailabilityDto;
import com.tractorstore.inventory.usecases.GetStockAvailabilityUseCase;
import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

  private final GetStockAvailabilityUseCase stock;

  public InventoryService(InventoryReadPort inventory) {
    this.stock = new GetStockAvailabilityUseCase(inventory);
  }

  public StockAvailabilityDto getStock(String sku) {
    String key = sku == null ? "" : sku.strip();
    return new StockAvailabilityDto(key, stock.availableUnits(key), stock.inStock(key));
  }
}
