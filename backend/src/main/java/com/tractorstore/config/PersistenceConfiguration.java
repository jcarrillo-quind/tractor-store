package com.tractorstore.config;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.bootstrap.adapters.ClasspathJsonSeedDataAdapter;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.inventory.usecases.ports.InventoryReadPort;
import com.tractorstore.order.adapters.InMemoryOrderAdapter;
import com.tractorstore.order.usecases.ports.OrderReadPort;
import com.tractorstore.order.usecases.ports.OrderWritePort;
import com.tractorstore.persistence.adapter.JpaCatalogReadAdapter;
import com.tractorstore.persistence.adapter.JpaInventoryReadAdapter;
import com.tractorstore.persistence.adapter.JpaStoreReadAdapter;
import com.tractorstore.persistence.mapper.PersistenceMapper;
import com.tractorstore.persistence.repository.CategoryJpaRepository;
import com.tractorstore.persistence.repository.InventoryJpaRepository;
import com.tractorstore.persistence.repository.ProductJpaRepository;
import com.tractorstore.persistence.repository.StoreJpaRepository;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fábrica central del switch memoria / PostgreSQL. Expone puertos de dominio y {@link SeedBundle}
 * para el resto de la aplicación.
 */
@Configuration
public class PersistenceConfiguration {

  private static final String MODE_PROPERTY = "tractor-store.persistence.mode";

  @Bean
  @ConditionalOnProperty(name = MODE_PROPERTY, havingValue = "inmemory")
  PersistencePorts inMemoryPersistencePorts() {
    SeedBundle bundle = ClasspathJsonSeedDataAdapter.load("seed-data.json");
    return new PersistencePorts(
        bundle.catalogReadPort(), bundle.storeReadPort(), bundle.inventoryReadPort());
  }

  @Configuration
  @ConditionalOnProperty(name = MODE_PROPERTY, havingValue = "postgres", matchIfMissing = true)
  static class PostgresPersistenceFactory {

    @Bean
    PersistenceMapper persistenceMapper() {
      return new PersistenceMapper();
    }

    @Bean
    JpaCatalogReadAdapter jpaCatalogReadAdapter(
        ProductJpaRepository products, CategoryJpaRepository categories, PersistenceMapper mapper) {
      return new JpaCatalogReadAdapter(products, categories, mapper);
    }

    @Bean
    JpaStoreReadAdapter jpaStoreReadAdapter(StoreJpaRepository stores, PersistenceMapper mapper) {
      return new JpaStoreReadAdapter(stores, mapper);
    }

    @Bean
    JpaInventoryReadAdapter jpaInventoryReadAdapter(InventoryJpaRepository inventory) {
      return new JpaInventoryReadAdapter(inventory);
    }

    @Bean
    PersistencePorts postgresPersistencePorts(
        JpaCatalogReadAdapter catalog,
        JpaStoreReadAdapter store,
        JpaInventoryReadAdapter inventory) {
      return new PersistencePorts(catalog, store, inventory);
    }
  }

  @Bean
  CatalogReadPort catalogReadPort(PersistencePorts ports) {
    return ports.catalog();
  }

  @Bean
  StoreReadPort storeReadPort(PersistencePorts ports) {
    return ports.store();
  }

  @Bean
  InventoryReadPort inventoryReadPort(PersistencePorts ports) {
    return ports.inventory();
  }

  @Bean
  SeedBundle seedBundle(PersistencePorts ports) {
    InMemoryOrderAdapter orders = new InMemoryOrderAdapter();
    return new SeedBundle(ports.catalog(), ports.store(), ports.inventory(), orders, orders);
  }

  @Bean
  OrderWritePort orderWritePort(SeedBundle seedBundle) {
    return seedBundle.orderWritePort();
  }

  @Bean
  OrderReadPort orderReadPort(SeedBundle seedBundle) {
    return seedBundle.orderReadPort();
  }
}
