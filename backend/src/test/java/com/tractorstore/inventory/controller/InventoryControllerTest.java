package com.tractorstore.inventory.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tractorstore.support.PostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class InventoryControllerTest extends PostgresIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void stock_inStockSku() throws Exception {
    mockMvc
        .perform(get("/api/inventory/AGR-100-RED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sku").value("AGR-100-RED"))
        .andExpect(jsonPath("$.availableUnits").value(4))
        .andExpect(jsonPath("$.inStock").value(true));
  }

  @Test
  void stock_outOfStockSku() throws Exception {
    mockMvc
        .perform(get("/api/inventory/AGR-100-GREEN"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.inStock").value(false))
        .andExpect(jsonPath("$.availableUnits").value(0));
  }
}
