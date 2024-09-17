package com.order_management_service.controller;

import com.order_management_service.dto.CustomizedOptionDto;
import com.order_management_service.service.CustomizedOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customized-option")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomizedOptionController {

    private final CustomizedOptionService customizedOptionService;

    @GetMapping("/get-all-customized-options")
    public ResponseEntity<?> getAllCustomizedOptions() {
        try {
            var customizedOptions = customizedOptionService.getAllCustomizedOptions();
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
}
