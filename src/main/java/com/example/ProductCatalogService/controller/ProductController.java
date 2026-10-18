package com.example.ProductCatalogService.controller;

import com.example.ProductCatalogService.dto.CategoryDto;
import com.example.ProductCatalogService.dto.ProductDto;
import com.example.ProductCatalogService.dto.ProductPitchDto;
import com.example.ProductCatalogService.model.Product;
import com.example.ProductCatalogService.service.PitchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ProductCatalogService.service.IproductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IproductService productService;
    private final PitchService pitchService;

    public ProductController(IproductService productService, PitchService pitchService) {
        this.productService = productService;
        this.pitchService = pitchService;
    }

    @GetMapping
    List<Product> getAllProducts() {
        return List.of();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id)
    {
         if(id<0)
         {
            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
         Product product = productService.getProductById(id);
         if(product==null)
         {
             return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         return new ResponseEntity<>(from(product),HttpStatus.OK);
    }


    private ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription(product.getCategory().getDescription());
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setName(product.getCategory().getName());

        productDto.setCategory(categoryDto);
        return productDto;
    }

    @GetMapping("/{id}/pitch")
    public ResponseEntity<ProductPitchDto> generatePitch(@PathVariable Long id) {
        if(id<0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Product product = productService.getProductById(id);
        if(product==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ProductPitchDto productPitchDto = new ProductPitchDto();
        productPitchDto.setId(product.getId());
        productPitchDto.setPitch(pitchService.generatePitch(product));
        return new ResponseEntity<>(productPitchDto,HttpStatus.OK);
    }


}
