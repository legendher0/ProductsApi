package com.inallmedia.productapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inallmedia.productapi.generated.model.CreateProduct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductApiIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  @Rollback
  void shouldReturnEmptyListWhenNothingIsSavedMessage() throws Exception {
    this.mockMvc.perform(get("/api/products")).andDo(print()).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("[]"));

  }

  @Test
  @Rollback
  void shouldReturnProductSavedWhenSaving() throws Exception {
    this.mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(getTestCreateProduct()))).andDo(print()).andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(containsString("1234567890")));
  }

  @Test
  @Rollback
  void shouldReturnProductSavedWhenModifying() throws Exception {
    var productToUpdate = getTestCreateProduct();
    productToUpdate.setItem("modified item");
    this.mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(getTestCreateProduct()))).andDo(print());
    this.mockMvc.perform(put("/api/products/1234567890").contentType(MediaType.APPLICATION_JSON).content(asJsonString(productToUpdate))).andDo(print()).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(containsString("modified item")));
  }

  @Test
  @Rollback
  void shouldReturnOkStatusWhenDeletingAndListIsEmpty() throws Exception {
    this.mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(getTestCreateProduct()))).andDo(print()).andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(containsString("1234567890")));
    this.mockMvc.perform(delete("/api/products/1234567890")).andDo(print()).andExpect(status().isNoContent());
    this.mockMvc.perform(get("/api/products")).andDo(print()).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("[]"));
  }

  @Test
  @Rollback
  void shouldReturnOkStatusAndProductWhenSingleProductIsRequestedByBarcode() throws Exception {
    this.mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(getTestCreateProduct()))).andDo(print()).andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(containsString("1234567890")));
    this.mockMvc.perform(get("/api/products/1234567890")).andDo(print()).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(containsString("1234567890")));
  }

  @Test
  @Rollback
  void shouldReturnProductsInPriceRange() throws Exception {
    var product1 = getTestCreateProduct();
    product1.setBarcode("1");
    product1.setPrice(100);
    var product2 = getTestCreateProduct();
    product2.setBarcode("2");
    product2.setPrice(200);
    var product3 = getTestCreateProduct();
    product3.setBarcode("3");
    product3.setPrice(300);
    this.mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product1))).andDo(print()).andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(containsString("\"barcode\":\"1\"")));
    this.mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product2))).andDo(print()).andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(containsString("\"barcode\":\"2\"")));
    this.mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(asJsonString(product3))).andDo(print()).andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.content().string(containsString("\"barcode\":\"3\"")));
    this.mockMvc.perform(get("/api/products/99/201")).andDo(print()).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(containsString("\"barcode\":\"1\"")))
            .andExpect(MockMvcResultMatchers.content().string(containsString("\"barcode\":\"2\"")))
            .andExpect(MockMvcResultMatchers.content().string(not(containsString("\"barcode\":\"3\""))));
    this.mockMvc.perform(get("/api/products/500/600")).andDo(print()).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("[]"));
  }

  private CreateProduct getTestCreateProduct() {
    return CreateProduct.builder().barcode("1234567890").item("test item").category("test category").price(100).discount(10).available(1).build();
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
