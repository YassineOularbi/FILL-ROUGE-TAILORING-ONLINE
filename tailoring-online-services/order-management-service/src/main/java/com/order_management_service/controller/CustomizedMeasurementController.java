package com.order_management_service.controller;

import com.order_management_service.dto.CustomizedMeasurementDto;
import com.order_management_service.service.elasticsearch.CustomizedMeasurementElasticsearchService;
import com.order_management_service.service.jpa.CustomizedMeasurementJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customized-measurement")
@RequiredArgsConstructor
public class CustomizedMeasurementController {

    private final CustomizedMeasurementJpaService customizedMeasurementService;
    private final CustomizedMeasurementElasticsearchService elasticsearchService;

    @GetMapping("/get-all-customized-measurements")
    public ResponseEntity<?> getAllCustomizedMeasurements(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var customizedMeasurements = customizedMeasurementService.getAllCustomizedMeasurements(page, size, sortField, sortDirection);
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

    @PostMapping("/add-customized-measurement/{measurementId}/{customizedProductId}")
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
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection,
            @RequestParam(required = false) String measurementIdFilter,
            @RequestParam(required = false) Double valueFilter,
            @RequestParam(required = false) String unitFilter,
            @RequestParam(required = false) Long productIdFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, Long.valueOf(measurementIdFilter), valueFilter, unitFilter, productIdFilter);
            return ResponseEntity.ok(results);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam("input") String input) {
        try {
            var suggestions = elasticsearchService.autocomplete(input);
            return ResponseEntity.ok(suggestions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
