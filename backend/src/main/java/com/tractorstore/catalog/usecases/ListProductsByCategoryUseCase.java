package com.tractorstore.catalog.usecases;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;

/**
 * Lista productos filtrando por categoría (p.ej. classic, autonomous).
 */
public final class ListProductsByCategoryUseCase {

	private final CatalogReadPort catalog;

	public ListProductsByCategoryUseCase(CatalogReadPort catalog) {
		this.catalog = Objects.requireNonNull(catalog, "catalog");
	}

	public List<Product> execute(String categoryFilter) {
		List<Product> products = catalog.allProducts();
		if (categoryFilter == null || categoryFilter.isBlank()) {
			return List.copyOf(products);
		}
		String key = categoryFilter.strip().toLowerCase(Locale.ROOT);
		return products.stream().filter(p -> p.category().toLowerCase(Locale.ROOT).equals(key)).toList();
	}
}
