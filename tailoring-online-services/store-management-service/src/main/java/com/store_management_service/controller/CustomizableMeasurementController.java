package com.store_management_service.controller;

import com.store_management_service.service.elasticsearch.CustomizableMeasurementElasticsearchService;
import com.store_management_service.service.jpa.CustomizableMeasurementJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customizable-measurement")
@RequiredArgsConstructor
public class CustomizableMeasurementController {

    private final CustomizableMeasurementJpaService customizableMeasurementService;
    private final CustomizableMeasurementElasticsearchService elasticsearchService;

    @GetMapping("/get-all-customizable-measurement")
    public ResponseEntity<?> getAllCustomizableMeasurement(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id.modelId", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var customizableMeasurements = customizableMeasurementService.getAllCustomizableMeasurement(page, size, sortField, sortDirection);
            return ResponseEntity.ok(customizableMeasurements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-customizable-measurement-by-three-d-model/{id}")
    public ResponseEntity<?> getAllCustomizableMeasurementByThreeDModel(@PathVariable("id") String id,
                                                                        @RequestParam(defaultValue = "0", name = "page") int page,
                                                                        @RequestParam(defaultValue = "10", name = "size") int size,
                                                                        @RequestParam(defaultValue = "id.modelId", name = "sortField") String sortField,
                                                                        @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var customizableMeasurements = customizableMeasurementService.getAllCustomizableMeasurementByThreeDModel(Long.valueOf(id), page, size, sortField, sortDirection);
            return ResponseEntity.ok(customizableMeasurements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customizable-measurement-by-id/{threeDModelId}&{measurementId}")
    public ResponseEntity<?> getCustomizableMeasurementById(@PathVariable String threeDModelId, @PathVariable String measurementId) {
        try {
            var customizableMeasurement = customizableMeasurementService.getCustomizableMeasurementById(Long.valueOf(threeDModelId), Long.valueOf(measurementId));
            return ResponseEntity.ok(customizableMeasurement);
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

    @DeleteMapping("/delete-customizable-measurement/{threeDModelId}&{measurementId}")
    public ResponseEntity<?> deleteCustomizableMeasurement(@PathVariable String threeDModelId, @PathVariable String measurementId) {
        try {
            customizableMeasurementService.deleteCustomizableMeasurement(Long.valueOf(threeDModelId), Long.valueOf(measurementId));
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
            @RequestParam(defaultValue = "id.modelId", name = "sortField") String sortField,
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
            @RequestParam(defaultValue = "id.modelId", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection,
            @RequestParam(required = false) String modelIdFilter,
            @RequestParam(required = false) String measurementNameFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, modelIdFilter, measurementNameFilter);
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
