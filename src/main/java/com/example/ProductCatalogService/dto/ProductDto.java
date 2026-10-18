package com.example.ProductCatalogService.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductDto {
    private  String name;
    private  String description;
    private BigDecimal price;
    private CategoryDto category;
}
