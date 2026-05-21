package com.tractorstore.catalog.model;

/** Tienda física (alineado con el modelo TypeScript {@code Store}). */
public record StoreDto(String id, String name, String city, String addressLine, String imageUrl) {}
