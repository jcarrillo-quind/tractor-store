package com.tractorstore.catalog.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.bootstrap.adapters.ClasspathJsonSeedDataAdapter;
import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.Variant;

class CatalogUseCasesTest {

	private FindProductByIdUseCase findProduct;
	private ListProductsByCategoryUseCase byCategory;
	private RecommendVariantsByColorUseCase recommendations;

	@BeforeEach
	void setUp() {
		SeedBundle bundle = ClasspathJsonSeedDataAdapter.load("seed-data.json");
		var catalog = bundle.catalogReadPort();
		findProduct = new FindProductByIdUseCase(catalog);
		byCategory = new ListProductsByCategoryUseCase(catalog);
		recommendations = new RecommendVariantsByColorUseCase(catalog);
	}

	@Test
	void productById_returnsProduct() {
		assertTrue(findProduct.execute("autonomous-x1").isPresent());
		assertEquals("autonomous", findProduct.execute("autonomous-x1").orElseThrow().category());
	}

	@Test
	void productsByCategory_filtersCaseInsensitive() {
		List<Product> classic = byCategory.execute("classic");
		assertFalse(classic.isEmpty());
		assertTrue(classic.stream().allMatch(p -> p.category().equalsIgnoreCase("classic")));
	}

	@Test
	void recommendations_excludeSeedSkus_andOrderedByColorProximity() {
		List<Variant> recs = recommendations.execute(List.of("AGR-100-RED", "MIN-20-RED"), 20);
		assertFalse(recs.isEmpty());
		assertTrue(recs.stream().noneMatch(v -> v.sku().equals("AGR-100-RED") || v.sku().equals("MIN-20-RED")));
		assertTrue(recs.stream().anyMatch(v -> v.sku().equals("COM-50-ORANGE") || v.sku().equals("AGR-100-YELLOW")));
	}
}
