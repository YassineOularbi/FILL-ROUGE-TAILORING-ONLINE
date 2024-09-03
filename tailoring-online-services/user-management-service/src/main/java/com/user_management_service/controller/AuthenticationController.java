package com.user_management_service.controller;

import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.service.EmailService;
import com.user_management_service.service.KeycloakService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/auth")
public class AuthenticationController {

    private final KeycloakService keycloakService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            var response = keycloakService.login(authenticationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(STR."Invalid credentials : \{e.getMessage()}");
        }
    }

    @GetMapping(path = "/send-verification-code/{id}")
    public ResponseEntity<String> sendVerificationCode(@PathVariable("id") String id) {
        try {
            keycloakService.sendVerificationCode(id);
            return ResponseEntity.ok("Verification code sent to the registered email address.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(STR."Failed to send verification code: \{e.getMessage()}");
        }
    }

    @GetMapping(path = "/verify-email/{id}&{code}")
    public ResponseEntity<String> verifyEmail(@PathVariable("id") String id, @PathVariable("code") String code) {
        try {
            keycloakService.verifyEmail(id, code);
            return ResponseEntity.ok("Email verified");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(STR."Failed to send verify email: \{e.getMessage()}");
        }
    }

    @GetMapping(path = "/reset-password/{userId}")
    public String sendResetPassword(@PathVariable("userId") String userId) throws MessagingException, UnsupportedEncodingException {
        keycloakService.sendResetPassword(userId);
        return "Reset Password Link Send Successfully to Registered E-mail Id.";
    }
}