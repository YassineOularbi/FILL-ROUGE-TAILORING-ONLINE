package com.store_management_service.controller;

import com.store_management_service.service.ThreeDModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/three-d-model")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ThreeDModelController {

    private final ThreeDModelService threeDModelService;

    @GetMapping("/get-all-three-d-model")
    public ResponseEntity<?> getAllThreeDModel() {
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
}
