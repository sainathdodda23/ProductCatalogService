package model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product extends  BaseEntity{
    private String name;
    private Double price;
    private String descrption;
    private  String imageUrl;
    private Category category;
}