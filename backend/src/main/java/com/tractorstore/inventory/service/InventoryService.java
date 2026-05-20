package com.tractorstore.inventory.service;

import com.tractorstore.inventory.model.StockAvailabilityDto;
import com.tractorstore.inventory.repository.InMemoryInventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

  private final InMemoryInventoryRepository repository;

  public InventoryService(InMemoryInventoryRepository repository) {
    this.repository = repository;
  }

  public StockAvailabilityDto getStock(String sku) {
    String key = sku == null ? "" : sku.strip();
    int units = repository.availableUnits(key);
    return new StockAvailabilityDto(key, units, repository.inStock(key));
  }
}
