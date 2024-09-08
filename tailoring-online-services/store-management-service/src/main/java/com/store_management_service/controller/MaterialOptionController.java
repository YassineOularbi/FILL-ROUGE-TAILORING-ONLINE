package com.store_management_service.controller;

import com.store_management_service.enums.MaterialType;
import com.store_management_service.service.MaterialOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material-option")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MaterialOptionController {
    
    private final MaterialOptionService materialOptionService;

    @GetMapping("/get-all-material-option")
    public ResponseEntity<?> getAllMaterialOptions(){
        try {
            var materialOptions = materialOptionService.getAllMaterialOptions();
            return ResponseEntity.ok(materialOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-material-option-by-customizable-option/{id}")
    public ResponseEntity<?> getAllMaterialOptionsByCustomizableOption(@PathVariable String id){
        try {
            var materialOptions = materialOptionService.getAllMaterialOptionsByCustomizableOption(Long.valueOf(id));
            return ResponseEntity.ok(materialOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-material-option-by-id/{materialId}&{optionId}")
    public ResponseEntity<?> getMaterialOptionById(@PathVariable String materialId, @PathVariable String optionId){
        try {
            var materialOption = materialOptionService.getMaterialOptionById(Long.valueOf(materialId), Long.valueOf(optionId));
            return ResponseEntity.ok(materialOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-material-option/{materialId}&{optionId}")
    public ResponseEntity<?> addMaterialOption(@PathVariable("materialId") String materialId, @PathVariable("optionId") String optionId){
        try {
            var materialOption = materialOptionService.addMaterialOption(Long.valueOf(materialId), Long.valueOf(optionId));
            return ResponseEntity.ok(materialOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-material-option/{materialId}&{optionId}")
    public ResponseEntity<?> deleteMaterialOption(@PathVariable("materialId") String materialId, @PathVariable("optionId") String optionId){
        try {
            materialOptionService.deleteMaterialOption(Long.valueOf(materialId), Long.valueOf(optionId));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
