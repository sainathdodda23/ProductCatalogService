package com.example.ProductCatalogService.controller;

import com.example.ProductCatalogService.service.ProductImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class InternalProductController {

      private final ProductImportService productImportService;

      @PostMapping("/import")
      public ResponseEntity<String> importProducts() {
            int count = productImportService.importProducts();
            return ResponseEntity.ok("Imported " + count + " products");
      }

}
