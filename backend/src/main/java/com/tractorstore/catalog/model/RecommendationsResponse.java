package com.tractorstore.catalog.model;

import java.util.List;

/** Respuesta de GET /api/catalog/recommendations. */
public record RecommendationsResponse(List<VariantDto> variants) {}
