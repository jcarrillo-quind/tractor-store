package com.tractorstore;

import java.util.List;

import com.tractorstore.bootstrap.SeedBundle;
import com.tractorstore.bootstrap.adapters.ClasspathJsonSeedDataAdapter;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.usecases.RecommendVariantsByColorUseCase;

/**
 * Demostración fase 1: recomendaciones por color a partir de un par de SKUs.
 */
public final class TractorStoreDemo {

	public static void main(String[] args) {
		SeedBundle data = ClasspathJsonSeedDataAdapter.load("seed-data.json");
		var recommendations = new RecommendVariantsByColorUseCase(data.catalogReadPort());

		String a = args.length > 0 ? args[0] : "AGR-100-RED";
		String b = args.length > 1 ? args[1] : "MIN-20-RED";

		System.out.println("SKUs de entrada: %s, %s".formatted(a, b));
		List<Variant> recs = recommendations.execute(List.of(a, b), 8);
		System.out.println("Recomendaciones por cercanía de color (excluye los SKUs de entrada):");
		for (Variant v : recs) {
			System.out.println(" - %s | %s | %s | %s".formatted(v.sku(), v.productId(), v.label(), v.colorHex()));
		}
	}

	private TractorStoreDemo() {
	}
}
