package com.product_management_service.controller;

import com.product_management_service.dto.MeasurementDto;
import com.product_management_service.service.MeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurements")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping
    public ResponseEntity<?> getAllMeasurements() {
        try {
            var measurements = measurementService.getAllMeasurements();
            return ResponseEntity.ok(measurements);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMeasurementById(@PathVariable("id") Long id) {
        try {
            var measurement = measurementService.getMeasurementById(id);
            return ResponseEntity.ok(measurement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addMeasurement(@RequestBody MeasurementDto measurementDto) {
        try {
            var addedMeasurement = measurementService.addMeasurement(measurementDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedMeasurement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeasurement(@PathVariable("id") Long id, @RequestBody MeasurementDto measurementDto) {
        try {
            var updatedMeasurement = measurementService.updateMeasurement(id, measurementDto);
            return ResponseEntity.ok(updatedMeasurement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMeasurement(@PathVariable("id") Long id) {
        try {
            measurementService.deleteMeasurement(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
