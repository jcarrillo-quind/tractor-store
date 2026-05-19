package com.tractorstore.catalog.repository;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.store.entities.Store;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/** Repositorio en memoria respaldado por {@code seed-data.json} (fase 3). */
@Repository
public class InMemoryCatalogRepository {

  private final CatalogReadPort catalog;
  private final StoreReadPort stores;

  public InMemoryCatalogRepository(SeedBundle seedBundle) {
    this.catalog = seedBundle.catalogReadPort();
    this.stores = seedBundle.storeReadPort();
  }

  public List<Product> findAllProducts() {
    return catalog.allProducts();
  }

  public Optional<Product> findProductById(String id) {
    if (id == null || id.isBlank()) {
      return Optional.empty();
    }
    return catalog.allProducts().stream().filter(p -> p.id().equals(id.strip())).findFirst();
  }

  public List<Product> findByCategory(String categoryFilter) {
    List<Product> all = catalog.allProducts();
    if (categoryFilter == null || categoryFilter.isBlank()) {
      return List.copyOf(all);
    }
    String key = categoryFilter.strip().toLowerCase(Locale.ROOT);
    return all.stream().filter(p -> p.category().toLowerCase(Locale.ROOT).equals(key)).toList();
  }

  public List<String> findDistinctCategories() {
    return catalog.allProducts().stream()
        .map(Product::category)
        .map(c -> c.toLowerCase(Locale.ROOT))
        .distinct()
        .sorted(Comparator.naturalOrder())
        .toList();
  }

  public List<Store> findAllStores() {
    return stores.allStores();
  }
}
