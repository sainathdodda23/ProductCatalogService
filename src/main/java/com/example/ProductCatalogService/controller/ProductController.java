package com.example.ProductCatalogService.controller;

import com.example.ProductCatalogService.dto.CategoryDto;
import com.example.ProductCatalogService.dto.ProductDto;
import com.example.ProductCatalogService.dto.ProductPitchDto;
import com.example.ProductCatalogService.model.Product;
import com.example.ProductCatalogService.service.PitchService;
import com.example.ProductCatalogService.service.StorageProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ProductCatalogService.service.IproductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IproductService productService;
    private final PitchService pitchService;
    private final IproductService storageService;

    public ProductController(  @Qualifier("dummyProductService")  IproductService productService,
                               PitchService pitchService,
                               @Qualifier("storageService") IproductService storageService) {
        this.productService = productService;
        this.pitchService = pitchService;
       this.storageService = storageService;
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
         Product product = storageService.getProductById(id);
         if(product==null)
         {
             return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         return new ResponseEntity<>(from(product),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
        Product product = storageService.createProduct(productDto);
        return new ResponseEntity<>(from(product),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable Long id)
    {
        boolean isDeleted = storageService.deleteByProductId(id);
        if(isDeleted)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
