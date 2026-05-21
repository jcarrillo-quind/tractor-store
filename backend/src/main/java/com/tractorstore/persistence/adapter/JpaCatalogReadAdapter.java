package com.tractorstore.persistence.adapter;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.model.CategoryMeta;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.persistence.entity.CategoryEntity;
import com.tractorstore.persistence.mapper.PersistenceMapper;
import com.tractorstore.persistence.repository.CategoryJpaRepository;
import com.tractorstore.persistence.repository.ProductJpaRepository;
import java.util.List;
import java.util.Optional;

/** Adaptador JPA para {@link CatalogReadPort}. */
public final class JpaCatalogReadAdapter implements CatalogReadPort {

  private final ProductJpaRepository products;
  private final CategoryJpaRepository categories;
  private final PersistenceMapper mapper;

  public JpaCatalogReadAdapter(
      ProductJpaRepository products, CategoryJpaRepository categories, PersistenceMapper mapper) {
    this.products = products;
    this.categories = categories;
    this.mapper = mapper;
  }

  @Override
  public List<Product> allProducts() {
    return products.findAllCatalog().stream().map(mapper::toDomain).toList();
  }

  @Override
  public Optional<Product> findProductById(String id) {
    if (id == null || id.isBlank()) {
      return Optional.empty();
    }
    return products.findByIdWithDetails(id.strip()).map(mapper::toDomain);
  }

  @Override
  public List<Product> findByCategory(String categoryFilter) {
    if (categoryFilter == null || categoryFilter.isBlank()) {
      return allProducts();
    }
    return products.findByCategoryId(categoryFilter.strip()).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public List<CategoryMeta> findAllCategories() {
    return categories.findAllByOrderByIdAsc().stream().map(this::toMeta).toList();
  }

  private CategoryMeta toMeta(CategoryEntity entity) {
    return new CategoryMeta(
        entity.getId(), entity.getLabel(), entity.getTagline(), entity.getImageUrl());
  }
}
