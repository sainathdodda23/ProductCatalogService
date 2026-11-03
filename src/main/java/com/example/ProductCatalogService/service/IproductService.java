package com.example.ProductCatalogService.service;

import com.example.ProductCatalogService.dto.ProductDto;
import com.example.ProductCatalogService.model.Product;

public interface IproductService {
    Product getProductById(Long id);

    Product createProduct(ProductDto productDto);
    boolean deleteByProductId(Long id);
}
