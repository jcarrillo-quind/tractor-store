package com.tractorstore.store.entities;

/** Punto de recogida / tienda física. */
public record Store(String id, String name, String city, String addressLine) {
  public Store {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("id requerido");
    }
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("name requerido");
    }
  }
}
