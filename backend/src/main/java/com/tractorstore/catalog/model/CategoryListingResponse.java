package com.tractorstore.catalog.model;

import java.util.List;

/** Respuesta de GET /api/catalog/categories/{filter}. */
public record CategoryListingResponse(
    String filter, List<ProductSummaryDto> products, List<CategoryFilterDto> availableFilters) {}
