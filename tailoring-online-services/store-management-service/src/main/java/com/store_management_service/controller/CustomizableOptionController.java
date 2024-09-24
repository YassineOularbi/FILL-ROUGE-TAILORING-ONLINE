package com.store_management_service.controller;

import com.store_management_service.enums.MaterialType;
import com.store_management_service.service.CustomizableOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customizable-option")
@RequiredArgsConstructor
public class CustomizableOptionController {

    private final CustomizableOptionService customizableOptionService;

    @GetMapping("/get-all-customizable-option")
    public ResponseEntity<?> getAllCustomizableOptions(){
        try {
            var customizableOptions = customizableOptionService.getAllCustomizableOptions();
            return ResponseEntity.ok(customizableOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-customizable-option-by-three-d-model/{id}")
    public ResponseEntity<?> getAllCustomizableOptionsByThreeDModel(@PathVariable String id){
        try {
            var customizableOptions = customizableOptionService.getAllCustomizableOptionsByThreeDModel(Long.valueOf(id));
            return ResponseEntity.ok(customizableOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customizable-option-by-id/{id}")
    public ResponseEntity<?> getCustomizableOptionById(@PathVariable String id){
        try {
            var customizableOption = customizableOptionService.getCustomizableOptionById(Long.valueOf(id));
            return ResponseEntity.ok(customizableOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-customizable-option/{id}&{type}")
    public ResponseEntity<?> addCustomizableOption(@PathVariable("id") String id, @PathVariable("type") MaterialType type){
        try {
            var customizableOption = customizableOptionService.addCustomizableOption(Long.valueOf(id), type);
            return ResponseEntity.ok(customizableOption);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-customizable-option/{id}")
    public ResponseEntity<?> deleteCustomizableOption(@PathVariable String id){
        try {
            customizableOptionService.deleteCustomizableOption(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
