package com.order_management_service.controller;

import com.order_management_service.dto.CustomizedOptionDto;
import com.order_management_service.service.elasticsearch.CustomizedOptionElasticsearchService;
import com.order_management_service.service.jpa.CustomizedOptionJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customized-option")
@RequiredArgsConstructor
public class CustomizedOptionController {

    private final CustomizedOptionJpaService customizedOptionService;
    private final CustomizedOptionElasticsearchService elasticsearchService;

    @GetMapping("/get-all-customized-options")
    public ResponseEntity<?> getAllCustomizedOptions(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var customizedOptions = customizedOptionService.getAllCustomizedOptions(page, size, sortField, sortDirection);
            return ResponseEntity.ok(customizedOptions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customized-option-by-id/{id}")
    public ResponseEntity<?> getCustomizedOptionById(@PathVariable("id") Long id) {
        try {
            var customizedOption = customizedOptionService.getCustomizedOptionById(id);
            return ResponseEntity.ok(customizedOption);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customized-option-with-details/{id}")
    public ResponseEntity<?> getCustomizedOptionWithDetails(@PathVariable("id") Long id) {
        try {
            var customizedOption = customizedOptionService.getCustomizedOptionWithDetails(id);
            return ResponseEntity.ok(customizedOption);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-customized-option/{productId}/{materialId}")
    public ResponseEntity<?> addCustomizedOption(@RequestBody CustomizedOptionDto customizedOptionDto, @PathVariable("productId") Long productId, @PathVariable("materialId") Long materialId) {
        try {
            var addedCustomizedOption = customizedOptionService.addCustomizedOption(customizedOptionDto, productId, materialId);
            return ResponseEntity.ok(addedCustomizedOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-customized-option/{id}")
    public ResponseEntity<?> updateCustomizedOption(@PathVariable("id") Long id, @RequestBody CustomizedOptionDto customizedOptionDto) {
        try {
            var updatedCustomizedOption = customizedOptionService.updateCustomizedOption(id, customizedOptionDto);
            return ResponseEntity.ok(updatedCustomizedOption);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-customized-option/{id}")
    public ResponseEntity<?> deleteCustomizedOption(@PathVariable("id") Long id) {
        try {
            customizedOptionService.deleteCustomizedOption(id);
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
            @RequestParam(required = false) String typeFilter,
            @RequestParam(required = false) Long materialIdFilter,
            @RequestParam(required = false) Long productIdFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, typeFilter, materialIdFilter, productIdFilter);
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
