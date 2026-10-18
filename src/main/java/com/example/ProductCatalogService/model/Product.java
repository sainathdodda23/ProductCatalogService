package com.example.ProductCatalogService.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Product extends  BaseEntity{
    private String name;
    private BigDecimal price;
    private String description;
    private  String imageUrl;
    private Category category;
}