package com.tractorstore.inventory.model;

/** Stock disponible para un SKU (alineado con el contrato del front). */
public record StockAvailabilityDto(String sku, int availableUnits, boolean inStock) {}
