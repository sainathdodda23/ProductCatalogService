package com.example.ProductCatalogService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class Product extends  BaseEntity{
    private String name;
    @Column(precision = 19,scale = 3)
    private BigDecimal price;
    private String description;
    private  String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}