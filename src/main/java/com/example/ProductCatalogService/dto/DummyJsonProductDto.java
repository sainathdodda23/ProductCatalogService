package com.example.ProductCatalogService.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DummyJsonProductDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private String thumbnail;
}
