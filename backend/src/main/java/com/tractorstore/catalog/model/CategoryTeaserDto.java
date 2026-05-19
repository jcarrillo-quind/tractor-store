package com.tractorstore.catalog.model;

import java.util.List;

/** Bloque de categoría destacada en el home. */
public record CategoryTeaserDto(
    String category, String title, String tagline, List<ProductSummaryDto> teasers) {}
