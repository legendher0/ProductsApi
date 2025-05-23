package com.inallmedia.productapi.mapper;

import com.inallmedia.productapi.generated.model.CreateProduct;
import com.inallmedia.productapi.generated.model.ReadProduct;
import com.inallmedia.productapi.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  Product mapToEntity(CreateProduct createProduct);
  ReadProduct mapToReadProduct(Product product);
}
