package com.store_management_service.controller;

import com.store_management_service.dto.StoreDto;
import com.store_management_service.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/get-all-stores")
    public ResponseEntity<?> getAllStores() {
        try {
            var stores = storeService.getAllStores();
            return ResponseEntity.ok(stores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-store-by-id/{id}")
    public ResponseEntity<?> getStoreById(@PathVariable("id") String id) {
        try {
            var store = storeService.getStoreById(Long.valueOf(id));
            return ResponseEntity.ok(store);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-store-with-tailor/{id}")
    public ResponseEntity<?> getStoreWithDetails(@PathVariable("id") String id) {
        try {
            var store = storeService.getStoreWithTailor(Long.valueOf(id));
            return ResponseEntity.ok(store);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-store/{id}")
    public ResponseEntity<?> addStore(@RequestBody StoreDto storeDto, @PathVariable String id) {
        try {
            var addedStore = storeService.addStore(storeDto, id);
            return ResponseEntity.ok(addedStore);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-store/{id}")
    public ResponseEntity<?> updateStore(@PathVariable("id") Long id, @RequestBody StoreDto storeDto) {
        try {
            var updatedStore = storeService.updateStore(id, storeDto);
            return ResponseEntity.ok(updatedStore);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-store/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable("id") Long id) {
        try {
            storeService.deleteStore(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
