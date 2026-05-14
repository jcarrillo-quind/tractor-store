package com.tractorstore.catalog.usecases;

import java.util.Objects;
import java.util.Optional;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;

public final class FindProductByIdUseCase {

	private final CatalogReadPort catalog;

	public FindProductByIdUseCase(CatalogReadPort catalog) {
		this.catalog = Objects.requireNonNull(catalog, "catalog");
	}

	public Optional<Product> execute(String id) {
		if (id == null || id.isBlank()) {
			return Optional.empty();
		}
		return catalog.allProducts().stream().filter(p -> p.id().equals(id)).findFirst();
	}
}
