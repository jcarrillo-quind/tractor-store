package com.tractorstore.catalog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class CatalogControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void home_returnsFeaturedCategories() throws Exception {
    mockMvc
        .perform(get("/api/catalog/home"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.featuredCategories").isArray())
        .andExpect(jsonPath("$.featuredCategories[0].category").exists())
        .andExpect(jsonPath("$.featuredCategories[0].teasers").isArray());
  }

  @Test
  void categoryListing_filtersClassic() throws Exception {
    mockMvc
        .perform(get("/api/catalog/categories/classic"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.filter").value("classic"))
        .andExpect(jsonPath("$.products").isArray())
        .andExpect(jsonPath("$.availableFilters").isArray());
  }

  @Test
  void productById_returnsDetail() throws Exception {
    mockMvc
        .perform(get("/api/catalog/products/agri-classic-100"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("agri-classic-100"))
        .andExpect(jsonPath("$.variants").isArray())
        .andExpect(jsonPath("$.variants[0].sku").value("AGR-100-RED"));
  }

  @Test
  void productById_notFound() throws Exception {
    mockMvc
        .perform(get("/api/catalog/products/unknown-id"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("NOT_FOUND"));
  }

  @Test
  void recommendations_returnsVariantsExcludingSeeds() throws Exception {
    mockMvc
        .perform(get("/api/catalog/recommendations").param("skus", "AGR-100-RED,MIN-20-RED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.variants").isArray())
        .andExpect(jsonPath("$.variants[0].sku").exists());
  }

  @Test
  void stores_returnsSeedStores() throws Exception {
    MvcResult result =
        mockMvc.perform(get("/api/catalog/stores")).andExpect(status().isOk()).andReturn();
    assertThat(result.getResponse().getContentAsString()).contains("store-bogota-norte");
  }
}
