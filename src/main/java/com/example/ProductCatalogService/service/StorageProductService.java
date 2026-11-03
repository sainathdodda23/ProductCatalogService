package com.example.ProductCatalogService.service;

import com.example.ProductCatalogService.dto.ProductDto;
import com.example.ProductCatalogService.model.Category;
import com.example.ProductCatalogService.model.Product;
import com.example.ProductCatalogService.model.enums.State;
import com.example.ProductCatalogService.repository.CategoryRepository;
import com.example.ProductCatalogService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service("storageService")
@RequiredArgsConstructor
public class StorageProductService implements IproductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findByIdAndState(id, State.ACTIVE).orElse(null);
    }

    @Override
    public Product createProduct(ProductDto productDto) {
        Product product = from(productDto);
        product.setCategory(findOrCreateCategory(product.getCategory()));
        product.setCreatedAt(new Date());
        product.setLastUpdatedAt(new Date());
        product.setState(State.ACTIVE);
        return productRepository.save(product);
    }

    @Override
    public boolean deleteByProductId(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return false;
        }
        product.setState(State.INACTIVE);
        product.setLastUpdatedAt(new Date());
        productRepository.save(product);
        return true;
    }

    private Category findOrCreateCategory(Category category) {
        if (category == null) {
            return null;
        }
        Optional<Category> existing =
                categoryRepository.findByName(category.getName());
        if (existing.isPresent()) {
            return existing.get();
        }
        category.setId(null);
        category.setCreatedAt(new Date());
        category.setLastUpdatedAt(new Date());
        return categoryRepository.save(category);
    }


    private Product from(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        if (dto.getCategory() != null) {
            Category category = new Category();
            category.setName(dto.getCategory().getName());
            category.setDescription(dto.getCategory().getDescription());
            product.setCategory(category);
        }
        return product;
    }
}
