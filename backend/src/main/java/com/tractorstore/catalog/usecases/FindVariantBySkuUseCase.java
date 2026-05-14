package com.tractorstore.catalog.usecases;

import java.util.Objects;
import java.util.Optional;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;

public final class FindVariantBySkuUseCase {

	private final CatalogReadPort catalog;

	public FindVariantBySkuUseCase(CatalogReadPort catalog) {
		this.catalog = Objects.requireNonNull(catalog, "catalog");
	}

	public Optional<Variant> execute(String sku) {
		if (sku == null || sku.isBlank()) {
			return Optional.empty();
		}
		for (Product p : catalog.allProducts()) {
			for (Variant v : p.variants()) {
				if (v.sku().equals(sku.strip())) {
					return Optional.of(v);
				}
			}
		}
		return Optional.empty();
	}
}
