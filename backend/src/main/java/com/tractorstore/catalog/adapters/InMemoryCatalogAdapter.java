package com.tractorstore.catalog.adapters;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.model.CategoryMeta;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/** Adaptador en memoria para {@link CatalogReadPort} (dataset cargado desde JSON). */
public final class InMemoryCatalogAdapter implements CatalogReadPort {

  private final List<Product> products;
  private final List<CategoryMeta> categories;

  public InMemoryCatalogAdapter(List<Product> products, List<CategoryMeta> categories) {
    this.products = List.copyOf(Objects.requireNonNull(products, "products"));
    this.categories = List.copyOf(Objects.requireNonNull(categories, "categories"));
  }

  @Override
  public List<Product> allProducts() {
    return products;
  }

  @Override
  public Optional<Product> findProductById(String id) {
    if (id == null || id.isBlank()) {
      return Optional.empty();
    }
    return products.stream().filter(p -> p.id().equals(id.strip())).findFirst();
  }

  @Override
  public List<Product> findByCategory(String categoryFilter) {
    if (categoryFilter == null || categoryFilter.isBlank()) {
      return List.copyOf(products);
    }
    String key = categoryFilter.strip().toLowerCase(Locale.ROOT);
    return products.stream()
        .filter(p -> p.category().toLowerCase(Locale.ROOT).equals(key))
        .toList();
  }

  @Override
  public List<CategoryMeta> findAllCategories() {
    return categories;
  }
}
