package com.tractorstore.catalog.adapters;

import java.util.List;
import java.util.Objects;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;

/**
 * Adaptador de persistencia en memoria para el catálogo (dataset cargado / fase 1).
 */
public final class InMemoryCatalogAdapter implements CatalogReadPort {

	private final List<Product> products;

	public InMemoryCatalogAdapter(List<Product> products) {
		this.products = List.copyOf(Objects.requireNonNull(products, "products"));
	}

	@Override
	public List<Product> allProducts() {
		return products;
	}
}
