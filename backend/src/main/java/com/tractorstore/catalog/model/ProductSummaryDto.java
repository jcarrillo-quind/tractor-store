package com.tractorstore.catalog.model;

import java.math.BigDecimal;
import java.util.List;

/** Resumen de producto para listados y teasers del home. */
public record ProductSummaryDto(
    String id,
    String name,
    String category,
    List<String> highlights,
    BigDecimal fromPrice,
    List<VariantDto> variants) {}
