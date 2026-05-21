package com.tractorstore.persistence.mapper;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.persistence.entity.ProductEntity;
import com.tractorstore.persistence.entity.StoreEntity;
import com.tractorstore.persistence.entity.VariantEntity;
import com.tractorstore.store.entities.Store;
import java.util.ArrayList;
import java.util.List;

public final class PersistenceMapper {

  public Product toDomain(ProductEntity entity) {
    String categoryId = entity.getCategory().getId();
    List<Variant> variants = new ArrayList<>();
    for (VariantEntity variant : entity.getVariants()) {
      variants.add(toVariant(variant, categoryId));
    }
    return new Product(
        entity.getId(), entity.getName(), categoryId, entity.getHighlights(), variants);
  }

  public Variant toVariant(VariantEntity entity, String productId) {
    return new Variant(
        entity.getSku(),
        entity.getLabel(),
        entity.getColorHex(),
        entity.getPrice(),
        productId,
        entity.getImageUrl());
  }

  public Store toDomain(StoreEntity entity) {
    return new Store(
        entity.getId(),
        entity.getName(),
        entity.getCity(),
        entity.getAddressLine(),
        entity.getImageUrl());
  }
}
