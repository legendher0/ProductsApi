package com.inallmedia.productapi.service;

import com.inallmedia.productapi.generated.api.ProductsApiDelegate;

import com.inallmedia.productapi.generated.model.CreateProduct;
import com.inallmedia.productapi.generated.model.ReadProduct;
import com.inallmedia.productapi.mapper.ProductMapper;
import com.inallmedia.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements ProductsApiDelegate {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public ReadProduct createProduct(CreateProduct createProduct) {
    var product = productMapper.mapToEntity(createProduct);
    return productMapper.mapToReadProduct(productRepository.save(product));
  }

  @Override
  public void deleteProduct(String id) {
    productRepository.delete(productRepository.findByIdOrFail(id));
  }

  @Override
  public List<ReadProduct> getAllProducts() {
    return productRepository.findAll().stream().map(productMapper::mapToReadProduct).toList();
  }

  @Override
  public ReadProduct getProduct(String id) {
    return productMapper.mapToReadProduct(productRepository.findById(id).orElseThrow());
  }

  @Override
  public ReadProduct updateProduct(String id, CreateProduct createProduct) {
    var product = productRepository.findByIdOrFail(id);
    product.setItem(createProduct.getItem() != null ? createProduct.getItem() : product.getItem());
    product.setCategory(createProduct.getCategory() != null ? createProduct.getCategory() : product.getCategory());
    product.setPrice(createProduct.getPrice() != null ? createProduct.getPrice() : product.getPrice());
    product.setDiscount(createProduct.getDiscount() != null ? createProduct.getDiscount() : product.getDiscount());
    product.setAvailable(createProduct.getAvailable() != null ? createProduct.getAvailable() : product.getAvailable());
    return productMapper.mapToReadProduct(productRepository.save(product));
  }

  @Override
  public List<ReadProduct> getAllProductsInRange(Integer bottomPrice, Integer topPrice) {
    return productRepository.findAllByPriceBetween(bottomPrice, topPrice).stream().map(productMapper::mapToReadProduct).toList();
  }
}
