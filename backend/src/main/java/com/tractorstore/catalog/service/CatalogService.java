package com.tractorstore.catalog.service;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.model.CategoryFilterDto;
import com.tractorstore.catalog.model.CategoryListingResponse;
import com.tractorstore.catalog.model.CategoryMeta;
import com.tractorstore.catalog.model.CategoryTeaserDto;
import com.tractorstore.catalog.model.HomeResponse;
import com.tractorstore.catalog.model.ProductDetailDto;
import com.tractorstore.catalog.model.ProductSummaryDto;
import com.tractorstore.catalog.model.RecommendationsResponse;
import com.tractorstore.catalog.model.StoreDto;
import com.tractorstore.catalog.model.VariantDto;
import com.tractorstore.catalog.usecases.RecommendVariantsByColorUseCase;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import com.tractorstore.store.entities.Store;
import com.tractorstore.store.usecases.ports.StoreReadPort;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

  private static final int HOME_TEASERS_PER_CATEGORY = 3;
  private static final int DEFAULT_RECOMMENDATION_LIMIT = 8;

  private final CatalogReadPort catalog;
  private final StoreReadPort stores;
  private final RecommendVariantsByColorUseCase recommendations;

  public CatalogService(CatalogReadPort catalog, StoreReadPort stores) {
    this.catalog = catalog;
    this.stores = stores;
    this.recommendations = new RecommendVariantsByColorUseCase(catalog);
  }

  public HomeResponse getHome() {
    Map<String, List<Product>> productsByCategory = new LinkedHashMap<>();
    for (Product product : catalog.allProducts()) {
      String key = product.category().toLowerCase(Locale.ROOT);
      productsByCategory.computeIfAbsent(key, k -> new ArrayList<>()).add(product);
    }

    List<CategoryTeaserDto> featured = new ArrayList<>();
    for (CategoryMeta category : catalog.findAllCategories()) {
      String categoryId = category.id();
      List<ProductSummaryDto> teasers =
          productsByCategory.getOrDefault(categoryId, List.of()).stream()
              .sorted(Comparator.comparing(Product::name))
              .limit(HOME_TEASERS_PER_CATEGORY)
              .map(this::toSummary)
              .toList();
      featured.add(
          new CategoryTeaserDto(
              categoryId, category.label(), category.tagline(), category.imageUrl(), teasers));
    }
    return new HomeResponse(featured);
  }

  public CategoryListingResponse getCategoryListing(String filter) {
    String normalized =
        filter == null || filter.isBlank() ? "" : filter.strip().toLowerCase(Locale.ROOT);
    List<Product> products = catalog.findByCategory(normalized);
    List<CategoryFilterDto> filters =
        catalog.findAllCategories().stream()
            .map(c -> new CategoryFilterDto(c.id(), c.label()))
            .toList();
    return new CategoryListingResponse(
        normalized, products.stream().map(this::toSummary).toList(), filters);
  }

  public Optional<ProductDetailDto> getProduct(String id) {
    return catalog.findProductById(id).map(this::toDetail);
  }

  public List<StoreDto> listStores() {
    return stores.allStores().stream().map(this::toStore).toList();
  }

  public RecommendationsResponse getRecommendations(String skusCsv, Integer limit) {
    int effectiveLimit =
        limit == null || limit <= 0 ? DEFAULT_RECOMMENDATION_LIMIT : Math.min(limit, 50);
    List<String> skus = parseSkus(skusCsv);
    List<VariantDto> variants =
        recommendations.execute(skus, effectiveLimit).stream().map(this::toVariant).toList();
    return new RecommendationsResponse(variants);
  }

  private static List<String> parseSkus(String skusCsv) {
    if (skusCsv == null || skusCsv.isBlank()) {
      return List.of();
    }
    return Arrays.stream(skusCsv.split(",")).map(String::strip).filter(s -> !s.isEmpty()).toList();
  }

  private ProductSummaryDto toSummary(Product product) {
    return new ProductSummaryDto(
        product.id(),
        product.name(),
        product.category(),
        product.highlights(),
        minPrice(product.variants()),
        product.variants().stream().map(this::toVariant).toList());
  }

  private ProductDetailDto toDetail(Product product) {
    return new ProductDetailDto(
        product.id(),
        product.name(),
        product.category(),
        product.highlights(),
        product.variants().stream().map(this::toVariant).toList());
  }

  private VariantDto toVariant(Variant variant) {
    return new VariantDto(
        variant.sku(),
        variant.label(),
        variant.colorHex(),
        variant.price(),
        variant.productId(),
        variant.imageUrl());
  }

  private StoreDto toStore(Store store) {
    return new StoreDto(
        store.id(), store.name(), store.city(), store.addressLine(), store.imageUrl());
  }

  private static BigDecimal minPrice(List<Variant> variants) {
    return variants.stream()
        .map(Variant::price)
        .min(Comparator.naturalOrder())
        .orElse(BigDecimal.ZERO);
  }
}
