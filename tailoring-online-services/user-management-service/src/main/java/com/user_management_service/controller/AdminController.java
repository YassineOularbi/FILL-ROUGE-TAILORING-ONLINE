package com.user_management_service.controller;

import com.user_management_service.dto.AdminDto;
import com.user_management_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get-all-admins")
    public ResponseEntity<?> getAllAdmins() {
        try {
            var admins = adminService.getAllAdmins();
            return ResponseEntity.ok(admins);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-admin-by-id/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable("id") String id) {
        try {
            var admin = adminService.getAdminById(id);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> addAdmin(@RequestBody AdminDto adminDto) {
        try {
            var addedAdmin = adminService.register(adminDto);
            return ResponseEntity.ok(addedAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-admin/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable("id") String id, @RequestBody AdminDto adminDto) {
        try {
            var updatedAdmin = adminService.updateAdmin(id, adminDto);
            return ResponseEntity.ok(updatedAdmin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-admin/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") String id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}