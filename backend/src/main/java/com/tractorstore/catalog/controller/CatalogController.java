package com.tractorstore.catalog.controller;

import com.tractorstore.catalog.model.CategoryListingResponse;
import com.tractorstore.catalog.model.HomeResponse;
import com.tractorstore.catalog.model.ProductDetailDto;
import com.tractorstore.catalog.model.RecommendationsResponse;
import com.tractorstore.catalog.model.StoreDto;
import com.tractorstore.catalog.service.CatalogService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

  private final CatalogService catalogService;

  public CatalogController(CatalogService catalogService) {
    this.catalogService = catalogService;
  }

  @GetMapping("/home")
  public HomeResponse home() {
    return catalogService.getHome();
  }

  @GetMapping("/categories/{filter}")
  public CategoryListingResponse categoryListing(@PathVariable String filter) {
    return catalogService.getCategoryListing(filter);
  }

  @GetMapping("/products/{id}")
  public ProductDetailDto product(@PathVariable String id) {
    return catalogService
        .getProduct(id)
        .orElseThrow(
            () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + id));
  }

  @GetMapping("/stores")
  public List<StoreDto> stores() {
    return catalogService.listStores();
  }

  @GetMapping("/recommendations")
  public RecommendationsResponse recommendations(
      @RequestParam(name = "skus") String skus,
      @RequestParam(name = "limit", required = false) Integer limit) {
    return catalogService.getRecommendations(skus, limit);
  }
}
