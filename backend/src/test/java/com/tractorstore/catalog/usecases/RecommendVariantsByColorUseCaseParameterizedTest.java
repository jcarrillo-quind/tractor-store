package com.tractorstore.catalog.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.tractorstore.catalog.entities.Product;
import com.tractorstore.catalog.entities.Variant;
import com.tractorstore.catalog.usecases.ports.CatalogReadPort;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests unitarios del algoritmo de recomendación por color (promedio RGB + distancia euclídea,
 * exclusiones de seed y orden por SKU ante empates).
 */
@ExtendWith(MockitoExtension.class)
class RecommendVariantsByColorUseCaseParameterizedTest {

  @Mock CatalogReadPort catalogPort;

  private RecommendVariantsByColorUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new RecommendVariantsByColorUseCase(catalogPort);
  }

  static Stream<Arguments> recommendationScenarios() {
    /*
     * Catálogo base: semilla roja (#FF0000); el mejor candidato restante cercano suele ser el tono oscuro (#BB0000).
     */
    Product baseline =
        Product.fromSeed(
            "tractor-x",
            "Tractor X",
            "classic",
            List.of(),
            List.of(
                new Variant("P-RED", "Rojo", "#FF0000", BigDecimal.ONE, "", ""),
                new Variant("P-SHADE", "Rojo oscuro", "#BB0000", BigDecimal.ONE, "", ""),
                new Variant("P-GREEN", "Verde", "#00DD00", BigDecimal.ONE, "", ""),
                new Variant("P-BLACK", "Negro", "#000000", BigDecimal.ONE, "", "")));

    return Stream.of(
        Arguments.of(
            "una semilla (#FF0000) — cercanías: shade, black, green",
            List.of(baseline),
            List.of("P-RED"),
            3,
            List.of("P-SHADE", "P-BLACK", "P-GREEN")),
        Arguments.of(
            "dos semillas rojos oscuros — mejor match restante antes que blanco largo",
            List.of(redPairCatalog()),
            List.of("P-A", "P-B"),
            2,
            List.of("P-GREEN", "P-WHITE")),
        Arguments.of(
            "límite cero ⇒ lista vacía", List.of(baseline), List.of("P-RED"), 0, List.of()),
        Arguments.of(
            "semillas inexistentes u omitidas ⇒ promedio (0,0,0)",
            List.of(baseline),
            List.of("NO-SKU", "", "  "),
            4,
            List.of("P-BLACK", "P-SHADE", "P-GREEN", "P-RED")),
        Arguments.of(
            "dos variantes mismo hex ⇒ desempate alfabético por SKU antes que un lejano",
            List.of(hexTieCatalog()),
            List.of("P-RED"),
            3,
            List.of("A-TWIN", "Z-TWIN", "P-CYAN")));
  }

  static Product redPairCatalog() {
    return Product.fromSeed(
        "tractor-y",
        "Tractor Y",
        "classic",
        List.of(),
        List.of(
            new Variant("P-A", "", "#660000", BigDecimal.ONE, "", ""),
            new Variant("P-B", "", "#660000", BigDecimal.ONE, "", ""),
            new Variant("P-GREEN", "", "#00BB00", BigDecimal.ONE, "", ""),
            new Variant("P-WHITE", "", "#F0F0F0", BigDecimal.ONE, "", "")));
  }

  /** Dos SKUs mismo gris medio; cyan lejos del rojo puro cuando la semilla se excluye. */
  static Product hexTieCatalog() {
    return Product.fromSeed(
        "tractor-tie",
        "Tractor Tie",
        "classic",
        List.of(),
        List.of(
            new Variant("P-RED", "", "#FF0000", BigDecimal.ONE, "", ""),
            new Variant("Z-TWIN", "", "#808080", BigDecimal.ONE, "", ""),
            new Variant("A-TWIN", "", "#808080", BigDecimal.ONE, "", ""),
            new Variant("P-CYAN", "", "#00FFFF", BigDecimal.ONE, "", "")));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("recommendationScenarios")
  void execute_returnsVariantsOrderedByRgbDistanceThenSku(
      String scenarioDescription,
      List<Product> catalog,
      List<String> seedSkus,
      int limit,
      List<String> expectedSkuOrder) {
    if (limit > 0) {
      when(catalogPort.allProducts()).thenReturn(catalog);
    }
    List<Variant> actual = useCase.execute(seedSkus, limit);
    assertThat(actual).extracting(Variant::sku).containsExactlyElementsOf(expectedSkuOrder);
  }
}
