package com.store_management_service.controller;

import com.store_management_service.service.CustomizableMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customizable-measurement")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomizableMeasurementController {

    private final CustomizableMeasurementService customizableMeasurementService;

    @GetMapping("/get-all-customizable-measurement")
    public ResponseEntity<?> getAllCustomizableMeasurement(){
        try {
            var customizableMeasurements = customizableMeasurementService.getAllCustomizableMeasurement();
            return ResponseEntity.ok(customizableMeasurements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-customizable-measurement-by-three-d-model/{id}")
    public ResponseEntity<?> getAllCustomizableMeasurementByThreeDModel(@PathVariable("id") String id){
        try {
            var customizableMeasurements = customizableMeasurementService.getAllCustomizableMeasurementByThreeDModel(Long.valueOf(id));
            return ResponseEntity.ok(customizableMeasurements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-customizable-measurement/{threeDModelId}&{measurementId}")
    public ResponseEntity<?> addCustomizableMeasurement(@PathVariable String threeDModelId, @PathVariable String measurementId) {
        try {
            var customizableMeasurement = customizableMeasurementService.addCustomizableMeasurement(Long.valueOf(threeDModelId), Long.valueOf(measurementId));
            return ResponseEntity.ok(customizableMeasurement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
