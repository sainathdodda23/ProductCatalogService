package com.example.ProductCatalogService.service;


import com.example.ProductCatalogService.dto.CategoryDto;
import com.example.ProductCatalogService.dto.ProductDto;
import com.example.ProductCatalogService.model.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProductImportService {
        private final IproductService  storageService;
        private final IproductService  supplierService;

        public  ProductImportService(IproductService storageService,
                    @Qualifier("dummyProductService") IproductService supplierService)
        {
            this.storageService = storageService;
            this.supplierService = supplierService;
        }

        public int importProducts()
        {
            int count=0;
            for(Product supplierProduct : supplierService.getAllProducts()){
                ProductDto dto = toImportDto(supplierProduct);
                storageService.createProduct(dto);
                count++;
            }
            return count;
        }

    private ProductDto toImportDto(Product product) {
        ProductDto dto = new ProductDto();
        //   dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            // categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            dto.setCategory(categoryDto);
        }
        return dto;
    }
}