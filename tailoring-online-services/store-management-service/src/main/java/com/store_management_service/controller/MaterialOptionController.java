package com.store_management_service.controller;

import com.store_management_service.service.jpa.MaterialOptionJpaService;
import com.store_management_service.service.elasticsearch.MaterialOptionElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material-option")
@RequiredArgsConstructor
public class MaterialOptionController {

    private final MaterialOptionJpaService materialOptionService;
    private final MaterialOptionElasticsearchService elasticsearchService;

    @GetMapping("/get-all-material-option")
    public ResponseEntity<?> getAllMaterialOptions(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id.materialId", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var materialOptions = materialOptionService.getAllMaterialOptions(page, size, sortField, sortDirection);
            return ResponseEntity.ok(materialOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-material-option-by-customizable-option/{id}")
    public ResponseEntity<?> getAllMaterialOptionsByCustomizableOption(@PathVariable String id) {
        try {
            var materialOptions = materialOptionService.getAllMaterialOptionsByCustomizableOption(Long.valueOf(id));
            return ResponseEntity.ok(materialOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-material-option-by-id/{materialId}&{optionId}")
    public ResponseEntity<?> getMaterialOptionById(@PathVariable String materialId, @PathVariable String optionId) {
        try {
            var materialOption = materialOptionService.getMaterialOptionById(Long.valueOf(materialId), Long.valueOf(optionId));
            return ResponseEntity.ok(materialOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-material-option/{materialId}&{optionId}")
    public ResponseEntity<?> addMaterialOption(@PathVariable("materialId") String materialId, @PathVariable("optionId") String optionId) {
        try {
            var materialOption = materialOptionService.addMaterialOption(Long.valueOf(materialId), Long.valueOf(optionId));
            return ResponseEntity.ok(materialOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-material-option/{materialId}&{optionId}")
    public ResponseEntity<?> deleteMaterialOption(@PathVariable("materialId") String materialId, @PathVariable("optionId") String optionId) {
        try {
            materialOptionService.deleteMaterialOption(Long.valueOf(materialId), Long.valueOf(optionId));
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
            @RequestParam(defaultValue = "id.materialId", name = "sortField") String sortField,
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
            @RequestParam(defaultValue = "id.materialId", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection,
            @RequestParam(required = false) String materialTypeFilter,
            @RequestParam(required = false) String optionIdFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, materialTypeFilter, optionIdFilter);
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
