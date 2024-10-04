package com.order_management_service.controller;

import com.order_management_service.dto.CustomizedProductDto;
import com.order_management_service.service.elasticsearch.CustomizedProductElasticsearchService;
import com.order_management_service.service.jpa.CustomizedProductJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customized-product")
@RequiredArgsConstructor
public class CustomizedProductController {

    private final CustomizedProductJpaService customizedProductService;
    private final CustomizedProductElasticsearchService elasticsearchService;

    @GetMapping("/get-all-customized-products")
    public ResponseEntity<?> getAllCustomizedProducts(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var customizedProducts = customizedProductService.getAllCustomizedProducts(page, size, sortField, sortDirection);
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

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam("input") String input,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var results = elasticsearchService.search(input, page, size, sortField, sortDirection);
            return ResponseEntity.ok(results);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection,
            @RequestParam(required = false) String productIdFilter,
            @RequestParam(required = false) String measurementFilter,
            @RequestParam(required = false) String optionFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, productIdFilter, measurementFilter, optionFilter);
            return ResponseEntity.ok(results);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("input") String input) {
        try {
            var suggestions = elasticsearchService.autocomplete(input);
            return ResponseEntity.ok(suggestions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
