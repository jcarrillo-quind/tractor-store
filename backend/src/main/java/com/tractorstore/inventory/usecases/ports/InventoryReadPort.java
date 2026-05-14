package com.tractorstore.inventory.usecases.ports;

/**
 * Consulta de stock por SKU (módulo inventory).
 */
public interface InventoryReadPort {

	int availableUnits(String sku);

	boolean inStock(String sku);
}
