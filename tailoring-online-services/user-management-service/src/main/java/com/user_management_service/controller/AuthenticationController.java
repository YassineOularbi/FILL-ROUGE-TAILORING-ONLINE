package com.user_management_service.controller;

import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.service.AuthenticationKeycloakService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users/api/auth")
public class AuthenticationController {

    private final AuthenticationKeycloakService keycloakService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            var response = keycloakService.login(authenticationRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(STR."Invalid credentials : \{e.getMessage()}");
        }
    }

    @GetMapping(path = "/send-verification-code/{id}")
    public ResponseEntity<String> sendVerificationCode(@PathVariable("id") String id) {
        try {
            var email = keycloakService.sendVerificationCode(id);
            return ResponseEntity.ok(STR."Verification code sent to the registered email address :\{email}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(path = "/verify-email/{id}&{code}")
    public ResponseEntity<String> verifyEmail(@PathVariable("id") String id, @PathVariable("code") String code) {
        try {
            var email = keycloakService.verifyEmail(id, code);
            return ResponseEntity.ok(STR."Email verified :\{email}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/send-otp-verification/{id}")
    public ResponseEntity<?> sendOTPByEmail(@PathVariable("id") String id) throws MessagingException, UnsupportedEncodingException {
        CompletableFuture<Boolean> emailSendingFuture = keycloakService.sendOtpVerification(id);
        try {
            boolean otpSent = emailSendingFuture.get();
            if (otpSent) {
                return ResponseEntity.ok().body("{\"message\": \"OTP sent successfully\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to send OTP\"}");
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp/{id}&{code}")
    public ResponseEntity<?> verifyOtpCode(@PathVariable("id") String id, @PathVariable("code") String code) {
        try {
            String email = keycloakService.verifyOtpCode(id, code);
            return ResponseEntity.ok().body(STR."{\"message\": \"OTP verified successfully\", \"email\": \"\{email}\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping(path = "/reset-password/{userId}")
//    public String sendResetPassword(@PathVariable("userId") String userId) throws MessagingException, UnsupportedEncodingException {
//        keycloakService.sendResetPassword(userId);
//        return "Reset Password Link Send Successfully to Registered E-mail Id.";
//    }
}