package com.tractorstore.catalog.model;

import java.util.List;

/** Detalle de producto para la PDP (alineado con {@code Product}). */
public record ProductDetailDto(
    String id, String name, String category, List<String> highlights, List<VariantDto> variants) {}
