package com.inallmedia.productapi.service;

import com.inallmedia.productapi.generated.model.CreateProduct;
import com.inallmedia.productapi.generated.model.ReadProduct;
import com.inallmedia.productapi.mapper.ProductMapper;
import com.inallmedia.productapi.models.Product;
import com.inallmedia.productapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;
  @Spy
  private ProductMapper productMapper;
  @InjectMocks
  private ProductService productService;

  @Test
  void createProduct() {
    var testCreateProduct = getTestCreateProduct();
    var testProduct = getTestProduct();
    var testReadProduct = getTestReadProduct();
    when(productRepository.save(any())).thenReturn(testProduct);
    when(productMapper.mapToReadProduct(testProduct)).thenReturn(testReadProduct);
    var productResponseEntity = productService.createProduct(testCreateProduct);
    assertNotNull(productResponseEntity);
    assertEquals(productResponseEntity.getBarcode(), testCreateProduct.getBarcode());
  }

  @Test
  void deleteProduct() {
    productService.deleteProduct("1234567890");
  }

  @Test
  void getAllProducts() {
    var testProduct = getTestProduct();
    when(productRepository.findAll()).thenReturn(List.of(testProduct));
    var products = productService.getAllProducts();
    assertNotNull(products);
  }

  @Test
  void getProduct() {
    var testProduct = getTestProduct();
    var testReadProduct = getTestReadProduct();
    when(productRepository.findById(any())).thenReturn(java.util.Optional.of(testProduct));
    when(productMapper.mapToReadProduct(testProduct)).thenReturn(testReadProduct);
    var product = productService.getProduct("1234567890");
    assertNotNull(product);
    assertEquals(product.getBarcode(), testProduct.getBarcode());
  }

  @Test
  void updateProduct() {
    var testCreateProduct = getTestCreateProduct();
    var testProduct = getTestProduct();
    var testReadProduct = getTestReadProduct();
    when(productRepository.findByIdOrFail(any())).thenReturn(testProduct);
    when(productMapper.mapToReadProduct(any())).thenReturn(testReadProduct);
    var productResponseEntity = productService.updateProduct("1234567890", testCreateProduct);
    assertNotNull(productResponseEntity);
    assertEquals(productResponseEntity.getBarcode(), testCreateProduct.getBarcode());
  }

  @Test
  void getAllProductsInRange() {
    var testProduct = getTestProduct();
    when(productRepository.findAllByPriceBetween(any(), any())).thenReturn(List.of(testProduct));
    var products = productService.getAllProductsInRange(100, 200);
    assertNotNull(products);
  }

  private Product getTestProduct() {
    return Product.builder().barcode("1234567890").item("test item").category("test category").price(100).discount(10).available(1).build();
  }

  private CreateProduct getTestCreateProduct() {
    return CreateProduct.builder().barcode("1234567890").item("test item").category("test category").price(100).discount(10).available(1).build();
  }

  private ReadProduct getTestReadProduct() {
    return ReadProduct.builder().barcode("1234567890").item("test item").category("test category").price(100).discount(10).available(1).build();
  }
}