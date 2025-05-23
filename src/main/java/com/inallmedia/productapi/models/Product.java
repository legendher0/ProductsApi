package com.inallmedia.productapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  @Id
  private String barcode;
  private String item;
  private String category;
  private Integer price;
  private Integer discount;
  private Integer available;
}
