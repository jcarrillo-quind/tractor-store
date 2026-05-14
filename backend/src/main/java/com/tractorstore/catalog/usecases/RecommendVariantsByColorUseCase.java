package com.tractorstore.catalog.usecases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.RgbColor;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;

/**
 * Recomendaciones por proximidad de color en RGB respecto al promedio de los SKUs dados.
 */
public final class RecommendVariantsByColorUseCase {

	private final CatalogReadPort catalog;
	private final FindVariantBySkuUseCase findVariant;

	public RecommendVariantsByColorUseCase(CatalogReadPort catalog) {
		this.catalog = Objects.requireNonNull(catalog, "catalog");
		this.findVariant = new FindVariantBySkuUseCase(catalog);
	}

	public List<Variant> execute(Collection<String> skus, int limit) {
		if (limit <= 0) {
			return List.of();
		}
		Set<String> seed = new HashSet<>();
		List<RgbColor> seedColors = new ArrayList<>();
		for (String sku : skus) {
			if (sku == null || sku.isBlank()) {
				continue;
			}
			findVariant.execute(sku.strip()).ifPresent(v -> {
				seed.add(v.sku());
				seedColors.add(RgbColor.fromHex(v.colorHex()));
			});
		}
		RgbColor target = RgbColor.average(seedColors);
		Set<String> exclude = new HashSet<>(seed);
		record Scored(Variant variant, double distance) {
		}
		List<Scored> ranked = new ArrayList<>();
		for (Product p : catalog.allProducts()) {
			for (Variant v : p.variants()) {
				if (exclude.contains(v.sku())) {
					continue;
				}
				RgbColor c = RgbColor.fromHex(v.colorHex());
				ranked.add(new Scored(v, target.distanceTo(c)));
			}
		}
		ranked.sort(Comparator.comparingDouble(Scored::distance).thenComparing(s -> s.variant().sku()));
		return ranked.stream().limit(limit).map(Scored::variant).toList();
	}
}
