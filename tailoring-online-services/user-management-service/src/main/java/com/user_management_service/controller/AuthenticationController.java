package com.user_management_service.controller;

import com.user_management_service.dto.CreateCustomerDto;
import com.user_management_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register-customer")
    public ResponseEntity<?> register(@Valid @RequestBody CreateCustomerDto createCustomerDto) {
        try {
            authenticationService.registerCustomer(createCustomerDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
