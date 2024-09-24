package com.order_management_service.controller;

import com.order_management_service.dto.CustomizedMeasurementDto;
import com.order_management_service.service.CustomizedMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customized-measurement")
@RequiredArgsConstructor
public class CustomizedMeasurementController {

    private final CustomizedMeasurementService customizedMeasurementService;

    @GetMapping("/get-all-customized-measurements")
    public ResponseEntity<?> getAllCustomizedMeasurements() {
        try {
            var customizedMeasurements = customizedMeasurementService.getAllCustomizedMeasurements();
            return ResponseEntity.ok(customizedMeasurements);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customized-measurement-by-id/{id}")
    public ResponseEntity<?> getCustomizedMeasurementById(@PathVariable("id") String id) {
        try {
            var customizedMeasurement = customizedMeasurementService.getCustomizedMeasurementById(Long.valueOf(id));
            return ResponseEntity.ok(customizedMeasurement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customized-measurement-with-details/{id}")
    public ResponseEntity<?> getCustomizedMeasurementWithDetails(@PathVariable("id") String id) {
        try {
            var customizedMeasurement = customizedMeasurementService.getCustomizedMeasurementWithDetails(Long.valueOf(id));
            return ResponseEntity.ok(customizedMeasurement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-customized-measurement/{measurementId}&{customizedProductId}")
    public ResponseEntity<?> addCustomizedMeasurement(@RequestBody CustomizedMeasurementDto customizedMeasurementDto, @PathVariable("measurementId") String measurementId, @PathVariable("customizedProductId") String customizedProductId) {
        try {
            var addedCustomizedMeasurement = customizedMeasurementService.addCustomizedMeasurement(customizedMeasurementDto, Long.valueOf(customizedProductId), Long.valueOf(measurementId));
            return ResponseEntity.ok(addedCustomizedMeasurement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-customized-measurement/{id}")
    public ResponseEntity<?> updateCustomizedMeasurement(@PathVariable("id") String id, @RequestBody CustomizedMeasurementDto customizedMeasurementDto) {
        try {
            var updatedCustomizedMeasurement = customizedMeasurementService.updateCustomizedMeasurement(Long.valueOf(id), customizedMeasurementDto);
            return ResponseEntity.ok(updatedCustomizedMeasurement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-customized-measurement/{id}")
    public ResponseEntity<?> deleteCustomizedMeasurement(@PathVariable("id") String id) {
        try {
            customizedMeasurementService.deleteCustomizedMeasurement(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
