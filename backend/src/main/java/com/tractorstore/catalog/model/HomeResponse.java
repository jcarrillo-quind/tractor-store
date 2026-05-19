package com.tractorstore.catalog.model;

import java.util.List;

/** Respuesta de GET /api/catalog/home. */
public record HomeResponse(List<CategoryTeaserDto> featuredCategories) {}
