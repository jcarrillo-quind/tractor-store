package com.tractorstore.catalog.service;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.model.CategoryFilterDto;
import com.tractorstore.catalog.model.CategoryListingResponse;
import com.tractorstore.catalog.model.CategoryTeaserDto;
import com.tractorstore.catalog.model.HomeResponse;
import com.tractorstore.catalog.model.ProductDetailDto;
import com.tractorstore.catalog.model.ProductSummaryDto;
import com.tractorstore.catalog.model.RecommendationsResponse;
import com.tractorstore.catalog.model.StoreDto;
import com.tractorstore.catalog.model.VariantDto;
import com.tractorstore.catalog.repository.InMemoryCatalogRepository;
import com.tractorstore.catalog.usecases.RecommendVariantsByColorUseCase;
import com.tractorstore.store.entities.Store;
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

  private static final Map<String, String> CATEGORY_LABELS =
      Map.of(
          "classic", "Clásicos",
          "autonomous", "Autónomos");

  private static final Map<String, String> CATEGORY_TAGLINES =
      Map.of(
          "classic", "Potencia probada para campo y finca",
          "autonomous", "Guiado preciso y telemetría integrada");

  private final InMemoryCatalogRepository repository;
  private final RecommendVariantsByColorUseCase recommendations;

  public CatalogService(InMemoryCatalogRepository repository, SeedBundle seedBundle) {
    this.repository = repository;
    this.recommendations = new RecommendVariantsByColorUseCase(seedBundle.catalogReadPort());
  }

  public HomeResponse getHome() {
    Map<String, List<Product>> byCategory = new LinkedHashMap<>();
    for (Product product : repository.findAllProducts()) {
      String key = product.category().toLowerCase(Locale.ROOT);
      byCategory.computeIfAbsent(key, k -> new ArrayList<>()).add(product);
    }
    List<CategoryTeaserDto> featured = new ArrayList<>();
    for (Map.Entry<String, List<Product>> entry : byCategory.entrySet()) {
      String category = entry.getKey();
      List<ProductSummaryDto> teasers =
          entry.getValue().stream()
              .sorted(Comparator.comparing(Product::name))
              .limit(HOME_TEASERS_PER_CATEGORY)
              .map(this::toSummary)
              .toList();
      featured.add(
          new CategoryTeaserDto(
              category, labelFor(category), CATEGORY_TAGLINES.getOrDefault(category, ""), teasers));
    }
    featured.sort(Comparator.comparing(CategoryTeaserDto::category));
    return new HomeResponse(featured);
  }

  public CategoryListingResponse getCategoryListing(String filter) {
    String normalized =
        filter == null || filter.isBlank() ? "" : filter.strip().toLowerCase(Locale.ROOT);
    List<Product> products = repository.findByCategory(normalized);
    List<CategoryFilterDto> filters =
        repository.findDistinctCategories().stream()
            .map(id -> new CategoryFilterDto(id, labelFor(id)))
            .toList();
    return new CategoryListingResponse(
        normalized, products.stream().map(this::toSummary).toList(), filters);
  }

  public Optional<ProductDetailDto> getProduct(String id) {
    return repository.findProductById(id).map(this::toDetail);
  }

  public List<StoreDto> listStores() {
    return repository.findAllStores().stream().map(this::toStore).toList();
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
        variant.sku(), variant.label(), variant.colorHex(), variant.price(), variant.productId());
  }

  private StoreDto toStore(Store store) {
    return new StoreDto(store.id(), store.name(), store.city(), store.addressLine());
  }

  private static BigDecimal minPrice(List<Variant> variants) {
    return variants.stream()
        .map(Variant::price)
        .min(Comparator.naturalOrder())
        .orElse(BigDecimal.ZERO);
  }

  private static String labelFor(String categoryId) {
    return CATEGORY_LABELS.getOrDefault(
        categoryId.toLowerCase(Locale.ROOT),
        categoryId.substring(0, 1).toUpperCase(Locale.ROOT) + categoryId.substring(1));
  }
}
