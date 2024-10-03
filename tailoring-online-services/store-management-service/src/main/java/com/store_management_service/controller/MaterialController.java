package com.store_management_service.controller;

import com.store_management_service.dto.MaterialDto;
import com.store_management_service.exception.MaterialNotFoundException;
import com.store_management_service.service.jpa.MaterialJpaService;
import com.store_management_service.service.elasticsearch.MaterialElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialJpaService materialService;
    private final MaterialElasticsearchService elasticsearchService;

    @GetMapping("/get-all-materials")
    public ResponseEntity<?> getAllMaterials(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var materials = materialService.getAllMaterials(page, size, sortField, sortDirection);
            return ResponseEntity.ok(materials);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-materials-by-store/{id}")
    public ResponseEntity<?> getAllMaterialsByStore(@PathVariable("id") String id,
                                                    @RequestParam(defaultValue = "0", name = "page") int page,
                                                    @RequestParam(defaultValue = "10", name = "size") int size,
                                                    @RequestParam(defaultValue = "id", name = "sortField") String sortField,
                                                    @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var materials = materialService.getAllMaterialsByStore(Long.valueOf(id), page, size, sortField, sortDirection);
            return ResponseEntity.ok(materials);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-material-by-id/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable("id") Long id) {
        try {
            var material = materialService.getMaterialById(id);
            return ResponseEntity.ok(material);
        } catch (MaterialNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-material/{storeId}")
    public ResponseEntity<?> addMaterial(@RequestBody MaterialDto materialDto, @PathVariable("storeId") Long storeId) {
        try {
            var addedMaterial = materialService.addMaterial(materialDto, storeId);
            return ResponseEntity.ok(addedMaterial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-material/{id}")
    public ResponseEntity<?> updateMaterial(@PathVariable("id") Long id, @RequestBody MaterialDto materialDto) {
        try {
            var updatedMaterial = materialService.updateMaterial(id, materialDto);
            return ResponseEntity.ok(updatedMaterial);
        } catch (MaterialNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-material/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable("id") Long id) {
        try {
            materialService.deleteMaterial(id);
            return ResponseEntity.noContent().build();
        } catch (MaterialNotFoundException e) {
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
            @RequestParam(required = false) String typeFilter,
            @RequestParam(required = false) String storeIdFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, typeFilter, storeIdFilter);
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
