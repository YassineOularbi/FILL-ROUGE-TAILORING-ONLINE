package com.store_management_service.controller;

import com.store_management_service.service.jpa.ThreeDModelJpaService;
import com.store_management_service.service.elasticsearch.ThreeDModelElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/three-d-model")
@RequiredArgsConstructor
public class ThreeDModelController {

    private final ThreeDModelJpaService threeDModelService;
    private final ThreeDModelElasticsearchService elasticsearchService;

    @GetMapping("/get-all-three-d-model")
    public ResponseEntity<?> getAllThreeDModel(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "id", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var threeDModels = threeDModelService.getAllThreeDModel();
            return ResponseEntity.ok(threeDModels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-three-d-model-by-product/{id}")
    public ResponseEntity<?> getThreeDModelByProduct(@PathVariable("id") String id) {
        try {
            var threeDModel = threeDModelService.getThreeDModelByProduct(Long.valueOf(id));
            return ResponseEntity.ok(threeDModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-three-d-model-by-id/{id}")
    public ResponseEntity<?> getThreeDModelById(@PathVariable("id") String id) {
        try {
            var threeDModel = threeDModelService.getThreeDModelById(Long.valueOf(id));
            return ResponseEntity.ok(threeDModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-three-d-model/{id}")
    public ResponseEntity<?> addThreeDModel(@PathVariable("id") String id) {
        try {
            var addedThreeDModel = threeDModelService.addThreeDModel(Long.valueOf(id));
            return ResponseEntity.ok(addedThreeDModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-three-d-model/{id}")
    public ResponseEntity<?> deleteThreeDModel(@PathVariable("id") String id) {
        try {
            threeDModelService.deleteThreeDModel(Long.valueOf(id));
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
            @RequestParam(required = false) String productIdFilter) {
        try {
            var results = elasticsearchService.filter(page, size, sortField, sortDirection, productIdFilter);
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
