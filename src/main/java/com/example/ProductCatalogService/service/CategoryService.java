package com.example.ProductCatalogService.service;

import com.example.ProductCatalogService.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public Double getAvgPriceForCategory(String category) {
        return categoryRepository.findAvgPriceForCategory(category);
    }
}
