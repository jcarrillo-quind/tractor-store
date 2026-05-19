package com.tractorstore.bootstrap.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.catalog.adapters.InMemoryCatalogAdapter;
import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.inventory.adapters.InMemoryInventoryAdapter;
import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import com.tractorstore.order.adapters.InMemoryOrderAdapter;
import com.tractorstore.order.usecases.ports.OrderReadPort;
import com.tractorstore.order.usecases.ports.OrderWritePort;
import com.tractorstore.store.adapters.InMemoryStoreAdapter;
import com.tractorstore.store.entities.Store;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Adaptador de infraestructura: lee el JSON de semilla y materializa los puertos en memoria. */
public final class ClasspathJsonSeedDataAdapter {

  private static final ObjectMapper MAPPER =
      new ObjectMapper().registerModule(new JavaTimeModule());

  private ClasspathJsonSeedDataAdapter() {}

  public static SeedBundle load(String resourcePath) {
    Objects.requireNonNull(resourcePath, "resourcePath");
    try (InputStream in =
        ClasspathJsonSeedDataAdapter.class.getClassLoader().getResourceAsStream(resourcePath)) {
      if (in == null) {
        throw new IllegalArgumentException("Recurso no encontrado: " + resourcePath);
      }
      SeedJson root = MAPPER.readValue(in, SeedJson.class);
      List<Store> stores = new ArrayList<>();
      for (StoreJson s : root.stores == null ? List.<StoreJson>of() : root.stores) {
        stores.add(new Store(s.id, s.name, s.city, s.addressLine));
      }
      Map<String, Integer> inv = new HashMap<>();
      if (root.inventory != null) {
        root.inventory.forEach((k, v) -> inv.put(k, v == null ? 0 : v));
      }
      List<Product> products = new ArrayList<>();
      for (ProductJson p : root.products == null ? List.<ProductJson>of() : root.products) {
        List<Variant> loose = new ArrayList<>();
        for (VariantJson v : p.variants == null ? List.<VariantJson>of() : p.variants) {
          loose.add(new Variant(v.sku, v.label, v.colorHex, v.price, ""));
        }
        products.add(Product.fromSeed(p.id, p.name, p.category, p.highlights, loose));
      }
      CatalogReadPort catalog = new InMemoryCatalogAdapter(products);
      StoreReadPort storePort = new InMemoryStoreAdapter(stores);
      InventoryReadPort inventoryPort = new InMemoryInventoryAdapter(inv);
      InMemoryOrderAdapter orderStore = new InMemoryOrderAdapter();
      OrderWritePort orderWrite = orderStore;
      OrderReadPort orderRead = orderStore;
      return new SeedBundle(catalog, storePort, inventoryPort, orderWrite, orderRead);
    } catch (IOException e) {
      throw new IllegalStateException("No se pudo leer " + resourcePath, e);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  static final class SeedJson {
    public List<StoreJson> stores;
    public Map<String, Integer> inventory;
    public List<ProductJson> products;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  static final class StoreJson {
    public String id;
    public String name;
    public String city;
    public String addressLine;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  static final class ProductJson {
    public String id;
    public String name;
    public String category;
    public List<String> highlights;
    public List<VariantJson> variants;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  static final class VariantJson {
    public String sku;
    public String label;
    public String colorHex;
    public BigDecimal price;
  }
}
