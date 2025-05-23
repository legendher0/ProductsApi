package com.inallmedia.productapi.repository;

import com.inallmedia.productapi.exception.EntityNotFoundException;
import com.inallmedia.productapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
  @Transactional(readOnly = true)
  default Product findByIdOrFail(String productId) {
    return findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
  }

  List<Product> findAllByPriceBetween(Integer bottomPrice, Integer topPrice);
}
