package com.example.ProductCatalogService.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Category extends BaseEntity {
    private  String name;
    private String description;

}
