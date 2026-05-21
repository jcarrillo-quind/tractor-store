package com.tractorstore.catalog.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Producto de catálogo con variantes y destacados comerciales. */
public record Product(
    String id, String name, String category, List<String> highlights, List<Variant> variants) {

  public Product {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(category, "category");
    if (id.isBlank()) {
      throw new IllegalArgumentException("id requerido");
    }
    highlights = highlights == null ? List.of() : List.copyOf(highlights);
    variants = variants == null ? List.of() : List.copyOf(variants);
    for (Variant v : variants) {
      if (!id.equals(v.productId())) {
        throw new IllegalArgumentException(
            "Variant %s no pertenece al producto %s".formatted(v.sku(), id));
      }
    }
  }

  /**
   * Crea un producto a partir de datos de semilla.
   *
   * @param id El ID del producto.
   * @param name El nombre del producto.
   * @param category La categoría del producto.
   * @param highlights Los destacados comerciales del producto.
   * @param looseVariants Las variantes del producto.
   * @return El producto creado.
   */
  public static Product fromSeed(
      String id,
      String name,
      String category,
      List<String> highlights,
      List<Variant> looseVariants) {
    List<Variant> bound = new ArrayList<>(looseVariants.size());
    for (Variant v : looseVariants) {
      bound.add(
          new Variant(v.sku(), v.label(), v.colorHex(), v.price(), id, nullToEmpty(v.imageUrl())));
    }
    return new Product(id, name, category, highlights, bound);
  }

  private static String nullToEmpty(String value) {
    return value == null ? "" : value;
  }
}
