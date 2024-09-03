package com.user_management_service.controller;

import com.user_management_service.dto.TailorDto;
import com.user_management_service.service.TailorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/api/tailor")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TailorController {

    private final TailorService tailorService;

    @GetMapping("/get-all-tailors")
    public ResponseEntity<?> getAllTailors() {
        try {
            var tailors = tailorService.getAllTailors();
            return ResponseEntity.ok(tailors);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-tailor-by-id/{id}")
    public ResponseEntity<?> getTailorById(@PathVariable("id") String id) {
        try {
            var tailor = tailorService.getTailorById(id);
            return ResponseEntity.ok(tailor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> addTailor(@RequestBody TailorDto tailorDto) {
        try {
            var addedTailor = tailorService.register(tailorDto);
            return ResponseEntity.ok(addedTailor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-tailor/{id}")
    public ResponseEntity<?> updateTailor(@PathVariable("id") String id, @RequestBody TailorDto tailorDto) {
        try {
            var updatedTailor = tailorService.updateTailor(id, tailorDto);
            return ResponseEntity.ok(updatedTailor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-tailor/{id}")
    public ResponseEntity<?> deleteTailor(@PathVariable("id") String id) {
        try {
            tailorService.deleteTailor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
