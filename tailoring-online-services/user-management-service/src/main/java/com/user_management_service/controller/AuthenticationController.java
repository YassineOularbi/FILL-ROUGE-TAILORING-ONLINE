package com.user_management_service.controller;

import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.dto.CreateCustomerDto;
import com.user_management_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        var response = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register-customer", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<?> register(
            @Valid @RequestPart("createCustomerDto") CreateCustomerDto createCustomerDto,
            @RequestPart(required = false, name = "profilePicture") MultipartFile profilePicture
    ) throws IOException {
        authenticationService.registerCustomer(createCustomerDto, profilePicture);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
