package com.tractorstore.catalog.usecases.ports;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.model.CategoryMeta;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de lectura del catálogo (memoria o PostgreSQL según {@link
 * com.tractorstore.config.PersistenceConfiguration}).
 */
public interface CatalogReadPort {

  List<Product> allProducts();

  Optional<Product> findProductById(String id);

  List<Product> findByCategory(String categoryFilter);

  List<CategoryMeta> findAllCategories();
}
