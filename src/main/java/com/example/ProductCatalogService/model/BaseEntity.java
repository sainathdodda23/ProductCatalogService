package com.example.ProductCatalogService.model;

import lombok.Getter;
import lombok.Setter;
import com.example.ProductCatalogService.model.enums.State;

import java.util.Date;

@Setter
@Getter
public abstract class BaseEntity {
     private  Long id;
     private Date createdAt;
     private Date lastUpdatedAt;
     private State state;
}
