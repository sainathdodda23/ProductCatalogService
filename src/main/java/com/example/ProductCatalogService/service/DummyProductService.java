package com.example.ProductCatalogService.service;

import com.example.ProductCatalogService.dto.DummyJsonProductDto;
import com.example.ProductCatalogService.model.Category;
import com.example.ProductCatalogService.model.Product;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class DummyProductService implements  IproductService{

    private final RestTemplate restTemplate;

    public DummyProductService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public Product getProductById(Long id) {
        try
        {
            ResponseEntity<DummyJsonProductDto> response =
                    restTemplate.getForEntity("https://dummyjson.com/products/{id}",
                               DummyJsonProductDto.class,id);
            if(response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return from(response.getBody());
            }
        }catch(HttpClientErrorException e)
        {
            return null;
        }
        return null;
    }

    private Product from(DummyJsonProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(BigDecimal.valueOf(dto.getPrice()));
        product.setImageUrl(dto.getThumbnail());

        Category category = new Category();
        category.setName(dto.getCategory());
        product.setCategory(category);
        return product;
    }


}
