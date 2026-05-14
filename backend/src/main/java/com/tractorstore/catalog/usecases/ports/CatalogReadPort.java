package com.tractorstore.catalog.usecases.ports;

import java.util.List;

import com.tractorstore.catalog.entities.Product;

/**
 * Puerto de lectura del catálogo (implementado en capa de adaptadores).
 */
public interface CatalogReadPort {

	List<Product> allProducts();
}
