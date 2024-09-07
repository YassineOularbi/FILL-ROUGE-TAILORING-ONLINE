package com.store_management_service.controller;

import com.store_management_service.dto.MaterialDto;
import com.store_management_service.exception.MaterialNotFoundException;
import com.store_management_service.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping("/get-all-materials")
    public ResponseEntity<?> getAllMaterials() {
        try {
            var materials = materialService.getAllMaterials();
            return ResponseEntity.ok(materials);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-materials-by-store/{id}")
    public ResponseEntity<?> getAllMaterialsByStore(@PathVariable("id") String id) {
        try {
            var materials = materialService.getAllMaterialsByStore(Long.valueOf(id));
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
}

