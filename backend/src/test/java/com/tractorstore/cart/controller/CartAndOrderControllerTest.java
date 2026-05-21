package com.tractorstore.cart.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tractorstore.support.PostgresIntegrationTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
class CartAndOrderControllerTest extends PostgresIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void cartSession_cookieAndAddRemove() throws Exception {
    MvcResult created =
        mockMvc
            .perform(get("/api/cart"))
            .andExpect(status().isOk())
            .andExpect(cookie().exists("TRACTOR_CART_SESSION"))
            .andExpect(jsonPath("$.itemCount").value(0))
            .andReturn();

    Cookie session = created.getResponse().getCookie("TRACTOR_CART_SESSION");
    assertThat(session).isNotNull();
    assertThat(created.getResponse().getHeader("Set-Cookie")).contains("HttpOnly");

    mockMvc
        .perform(
            post("/api/cart/items")
                .cookie(session)
                .contentType(APPLICATION_JSON)
                .content("{\"sku\":\"AGR-100-RED\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.itemCount").value(1))
        .andExpect(jsonPath("$.lines[0].sku").value("AGR-100-RED"))
        .andExpect(jsonPath("$.lines[0].imageUrl").isNotEmpty());

    mockMvc
        .perform(get("/api/cart/mini").cookie(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.itemCount").value(1));

    mockMvc
        .perform(delete("/api/cart/items/AGR-100-RED").cookie(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.itemCount").value(0));
  }

  @Test
  void addItem_outOfStock_returnsConflict() throws Exception {
    Cookie session = sessionCookie();
    mockMvc
        .perform(
            post("/api/cart/items")
                .cookie(session)
                .contentType(APPLICATION_JSON)
                .content("{\"sku\":\"AGR-100-GREEN\"}"))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value("CONFLICT"));
  }

  @Test
  void placeOrder_clearsCartAndReturnsOrder() throws Exception {
    Cookie session = sessionCookie();
    mockMvc
        .perform(
            post("/api/cart/items")
                .cookie(session)
                .contentType(APPLICATION_JSON)
                .content("{\"sku\":\"MIN-20-RED\"}"))
        .andExpect(status().isOk());

    MvcResult placed =
        mockMvc
            .perform(
                post("/api/orders")
                    .cookie(session)
                    .contentType(APPLICATION_JSON)
                    .content(
                        """
                        {
                          "customerName": "Ana Test",
                          "customerEmail": "ana@test.com",
                          "pickupStoreId": "store-bogota-norte"
                        }
                        """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.total").exists())
            .andReturn();

    JsonNode order = objectMapper.readTree(placed.getResponse().getContentAsString());
    String orderId = order.get("id").asText();

    mockMvc
        .perform(get("/api/orders/" + orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customerEmail").value("ana@test.com"));

    mockMvc
        .perform(get("/api/cart").cookie(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.itemCount").value(0));
  }

  private Cookie sessionCookie() throws Exception {
    MvcResult result = mockMvc.perform(get("/api/cart")).andExpect(status().isOk()).andReturn();
    return result.getResponse().getCookie("TRACTOR_CART_SESSION");
  }
}
