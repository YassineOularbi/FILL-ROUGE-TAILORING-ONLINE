
package com.user_management_service.controller;

import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            var response = authenticationService.login(authenticationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping(path = "/send-verification-code/{id}")
    public ResponseEntity<?> sendVerificationCode(@PathVariable("id") String id) {
        try {
            var email = authenticationService.sendVerificationCode(id);
            return ResponseEntity.ok(String.format("Verification code sent to the registered email address : %s", email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(path = "/verify-email/{id}&{code}")
    public ResponseEntity<String> verifyEmail(@PathVariable("id") String id, @PathVariable("code") String code) {
        try {
            var email = authenticationService.verifyEmail(id, code);
            return ResponseEntity.ok(String.format("Email verified: %s", email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/send-otp-verification/{id}")
    public ResponseEntity<?> sendOTPByEmail(@PathVariable("id") String id) {
        try {
            var email = authenticationService.sendOtpVerification(id);
            return ResponseEntity.ok(String.format("OTP code sent to the registered email address: %s", email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp/{id}&{code}")
    public ResponseEntity<?> verifyOtpCode(@PathVariable("id") String id, @PathVariable("code") String code) {
        try {
            String email = authenticationService.verifyOtpCode(id, code);
            return ResponseEntity.ok().body(String.format("{\"message\": \"OTP verified successfully\", \"email\": \"%s\"}", email));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
