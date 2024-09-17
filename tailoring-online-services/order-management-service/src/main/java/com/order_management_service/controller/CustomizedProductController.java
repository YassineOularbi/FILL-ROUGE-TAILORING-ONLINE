package com.order_management_service.controller;

import com.order_management_service.dto.CustomizedProductDto;
import com.order_management_service.service.CustomizedProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customized-product")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomizedProductController {

    private final CustomizedProductService customizedProductService;

    @GetMapping("/get-all-customized-products")
    public ResponseEntity<?> getAllCustomizedProducts() {
        try {
            var customizedProducts = customizedProductService.getAllCustomizedProducts();
            return ResponseEntity.ok(customizedProducts);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customized-product-by-id/{id}")
    public ResponseEntity<?> getCustomizedProductById(@PathVariable("id") Long id) {
        try {
            var customizedProduct = customizedProductService.getCustomizedProductById(id);
            return ResponseEntity.ok(customizedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customized-product-with-product/{id}")
    public ResponseEntity<?> getCustomizedProductWithProduct(@PathVariable("id") Long id) {
        try {
            var customizedProduct = customizedProductService.getCustomizedProductWithProduct(id);
            return ResponseEntity.ok(customizedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-customized-product/{productId}")
    public ResponseEntity<?> addCustomizedProduct(@RequestBody CustomizedProductDto customizedProductDto, @PathVariable("productId") Long productId) {
        try {
            var addedCustomizedProduct = customizedProductService.addCustomizedProduct(customizedProductDto, productId);
            return ResponseEntity.ok(addedCustomizedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-customized-product/{id}")
    public ResponseEntity<?> updateCustomizedProduct(@PathVariable("id") Long id, @RequestBody CustomizedProductDto customizedProductDto) {
        try {
            var updatedCustomizedProduct = customizedProductService.updateCustomizedProduct(id, customizedProductDto);
            return ResponseEntity.ok(updatedCustomizedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-customized-product/{id}")
    public ResponseEntity<?> deleteCustomizedProduct(@PathVariable("id") Long id) {
        try {
            customizedProductService.deleteCustomizedProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
