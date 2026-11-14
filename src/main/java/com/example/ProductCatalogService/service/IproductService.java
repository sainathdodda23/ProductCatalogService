package com.example.ProductCatalogService.service;

import com.example.ProductCatalogService.dto.ProductDto;
import com.example.ProductCatalogService.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IproductService {
    Product getProductById(Long id);

    Product createProduct(ProductDto productDto);
    boolean deleteByProductId(Long id);

    List<Product> getAllProducts();

    Page<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
