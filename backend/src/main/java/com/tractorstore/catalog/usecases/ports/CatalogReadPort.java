package com.tractorstore.catalog.usecases.ports;

import com.tractorstore.catalog.entities.Product;
import java.util.List;

/** Puerto de lectura del catálogo (implementado en capa de adaptadores). */
public interface CatalogReadPort {

  List<Product> allProducts();
}
