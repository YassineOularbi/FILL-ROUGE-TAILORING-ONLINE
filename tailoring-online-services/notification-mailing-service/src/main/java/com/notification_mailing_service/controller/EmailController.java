package com.notification_mailing_service.controller;

import com.notification_mailing_service.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/api/email")
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping(path = "/send-verification-code/{email}")
    public CompletableFuture<ResponseEntity<?>> sendVerificationCode(@PathVariable("email") String email) throws MessagingException, UnsupportedEncodingException {
        return emailService.sendVerificationEmail(email).thenApply(success -> {
            if (success) {
                return ResponseEntity.ok(STR."{\"message\": \"Verification code sent to the registered email address: \{email}\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to send verification code\"}");
            }
        });
    }

    @GetMapping("/send-otp-verification/{email}")
    public CompletableFuture<ResponseEntity<?>> sendOTPByEmail(@PathVariable("email") String email) {
        return emailService.sendOTPByEmail(email).thenApply(otpSent -> {
            if (otpSent) {
                return ResponseEntity.ok(STR."{\"message\": \"OTP sent successfully to: \{email}\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to send OTP\"}");
            }
        });
    }

    @GetMapping(path = "/verify-code/{email}&{code}")
    public CompletableFuture<ResponseEntity<?>> verifyEmail(@PathVariable("email") String email, @PathVariable("code") String code) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                var isVerified = emailService.verifyCode(email, code);
                if (isVerified) {
                    return ResponseEntity.ok(STR."{\"message\": \"Email verified: \{email}\"}");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Invalid verification code\"}");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}");
            }
        });
    }

    @GetMapping("/contact-us")
    public CompletableFuture<ResponseEntity<?>> contactUs(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("message") String message){
        return CompletableFuture.supplyAsync(() -> {
            try {
                emailService.contactUs(name, email, phone, message);
                return ResponseEntity.ok("Email sent successfully!");
            } catch (MessagingException | UnsupportedEncodingException e) {
                return ResponseEntity.status(500).body(STR."Failed to send email: \{e.getMessage()}");
            }
        });
    }
}
