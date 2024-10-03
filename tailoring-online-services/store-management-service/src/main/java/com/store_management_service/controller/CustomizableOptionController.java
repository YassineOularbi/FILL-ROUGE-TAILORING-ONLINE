package com.store_management_service.controller;

import com.store_management_service.enums.MaterialType;
import com.store_management_service.service.jpa.CustomizableOptionJpaService;
import com.store_management_service.service.elasticsearch.CustomizableOptionElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customizable-option")
@RequiredArgsConstructor
public class CustomizableOptionController {

    private final CustomizableOptionJpaService customizableOptionService;
    private final CustomizableOptionElasticsearchService elasticsearchService;

    @GetMapping("/get-all-customizable-option")
    public ResponseEntity<?> getAllCustomizableOptions(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var customizableOptions = customizableOptionService.getAllCustomizableOptions(page, size, sortField, sortDirection);
            return ResponseEntity.ok(customizableOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-customizable-option-by-three-d-model/{id}")
    public ResponseEntity<?> getAllCustomizableOptionsByThreeDModel(@PathVariable String id) {
        try {
            var customizableOptions = customizableOptionService.getAllCustomizableOptionsByThreeDModel(Long.valueOf(id));
            return ResponseEntity.ok(customizableOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customizable-option-by-id/{id}")
    public ResponseEntity<?> getCustomizableOptionById(@PathVariable String id) {
        try {
            var customizableOption = customizableOptionService.getCustomizableOptionById(Long.valueOf(id));
            return ResponseEntity.ok(customizableOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-customizable-option/{id}&{type}")
    public ResponseEntity<?> addCustomizableOption(@PathVariable("id") String id, @PathVariable("type") MaterialType type) {
        try {
            var customizableOption = customizableOptionService.addCustomizableOption(Long.valueOf(id), type);
            return ResponseEntity.ok(customizableOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-customizable-option/{id}")
    public ResponseEntity<?> deleteCustomizableOption(@PathVariable String id) {
        try {
            customizableOptionService.deleteCustomizableOption(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection,
            @RequestParam(required = false) String modelIdFilter,
            @RequestParam(required = false) String typeFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, modelIdFilter, typeFilter);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("input") String input) {
        try {
            var suggestions = elasticsearchService.autocomplete(input);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
