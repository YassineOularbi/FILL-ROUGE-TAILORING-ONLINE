package com.store_management_service.controller;

import com.store_management_service.dto.MeasurementDto;
import com.store_management_service.service.jpa.MeasurementJpaService;
import com.store_management_service.service.elasticsearch.MeasurementElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/measurement")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementJpaService measurementService;
    private final MeasurementElasticsearchService elasticsearchService;

    @GetMapping("/get-all-measurements")
    public ResponseEntity<?> getAllMeasurements(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var measurements = measurementService.getAllMeasurements(page, size);
            return ResponseEntity.ok(measurements);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-measurement-by-id/{id}")
    public ResponseEntity<?> getMeasurementById(@PathVariable("id") Long id) {
        try {
            var measurement = measurementService.getMeasurementById(id);
            return ResponseEntity.ok(measurement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-measurement")
    public ResponseEntity<?> addMeasurement(@RequestBody MeasurementDto measurementDto) {
        try {
            var addedMeasurement = measurementService.addMeasurement(measurementDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedMeasurement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-measurement/{id}")
    public ResponseEntity<?> updateMeasurement(@PathVariable("id") Long id, @RequestBody MeasurementDto measurementDto) {
        try {
            var updatedMeasurement = measurementService.updateMeasurement(id, measurementDto);
            return ResponseEntity.ok(updatedMeasurement);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-measurement/{id}")
    public ResponseEntity<?> deleteMeasurement(@PathVariable("id") Long id) {
        try {
            measurementService.deleteMeasurement(id);
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
            @RequestParam(required = false) String nameFilter,
            @RequestParam(required = false) String descriptionFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, nameFilter, descriptionFilter);
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
