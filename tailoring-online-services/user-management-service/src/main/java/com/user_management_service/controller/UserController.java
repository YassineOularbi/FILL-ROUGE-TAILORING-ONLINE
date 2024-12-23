//package com.user_management_service.controller;
//
//import com.user_management_service.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/user")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @GetMapping("/get-all-users")
//    public ResponseEntity<?> getAllUsers() {
//        try {
//            var users = userService.getAllUsers();
//            return ResponseEntity.ok(users);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/get-user-by-id/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
//        try {
//            var user = userService.getUserById(id);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//}
