package com.notification_mailing_service;

import com.notification_mailing_service.controller.EmailController;
import com.notification_mailing_service.service.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailControllerTests {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendVerificationCode_Success() throws MessagingException, UnsupportedEncodingException {
        String email = "test@example.com";
        when(emailService.sendVerificationEmail(email)).thenReturn(CompletableFuture.completedFuture(true));

        CompletableFuture<ResponseEntity<?>> response = emailController.sendVerificationCode(email);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertTrue(Objects.requireNonNull(response.join().getBody()).toString().contains("Verification code sent to the registered email address"));
    }

    @Test
    void sendVerificationCode_Failure() throws MessagingException, UnsupportedEncodingException {
        String email = "test@example.com";
        when(emailService.sendVerificationEmail(email)).thenReturn(CompletableFuture.completedFuture(false));

        CompletableFuture<ResponseEntity<?>> response = emailController.sendVerificationCode(email);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.join().getStatusCode());
        assertTrue(Objects.requireNonNull(response.join().getBody()).toString().contains("Failed to send verification code"));
    }

    @Test
    void sendOTPByEmail_Success() {
        String email = "test@example.com";
        when(emailService.sendOTPByEmail(email)).thenReturn(CompletableFuture.completedFuture(true));

        CompletableFuture<ResponseEntity<?>> response = emailController.sendOTPByEmail(email);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertTrue(Objects.requireNonNull(response.join().getBody()).toString().contains("OTP sent successfully"));
    }

    @Test
    void sendOTPByEmail_Failure() {
        String email = "test@example.com";
        when(emailService.sendOTPByEmail(email)).thenReturn(CompletableFuture.completedFuture(false));

        CompletableFuture<ResponseEntity<?>> response = emailController.sendOTPByEmail(email);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.join().getStatusCode());
        assertTrue(Objects.requireNonNull(response.join().getBody()).toString().contains("Failed to send OTP"));
    }

    @Test
    void verifyEmail_Success() {
        String email = "test@example.com";
        String code = "123456";
        when(emailService.verifyCode(email, code)).thenReturn(true);

        CompletableFuture<ResponseEntity<?>> response = emailController.verifyEmail(email, code);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertTrue(Objects.requireNonNull(response.join().getBody()).toString().contains("Email verified"));
    }

    @Test
    void verifyEmail_InvalidCode() {
        String email = "test@example.com";
        String code = "123456";
        when(emailService.verifyCode(email, code)).thenReturn(false);

        CompletableFuture<ResponseEntity<?>> response = emailController.verifyEmail(email, code);

        assertEquals(HttpStatus.BAD_REQUEST, response.join().getStatusCode());
        assertTrue(Objects.requireNonNull(response.join().getBody()).toString().contains("Invalid verification code"));
    }

    @Test
    void contactUs_Success() throws MessagingException, UnsupportedEncodingException {
        String name = "John Doe";
        String email = "test@example.com";
        String phone = "123456789";
        String message = "Test message";
        doNothing().when(emailService).contactUs(name, email, phone, message);

        CompletableFuture<ResponseEntity<?>> response = emailController.contactUs(name, email, phone, message);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertEquals("Email sent successfully!", response.join().getBody());
    }

    @Test
    void contactUs_Failure() throws MessagingException, UnsupportedEncodingException {
        String name = "John Doe";
        String email = "test@example.com";
        String phone = "123456789";
        String message = "Test message";
        doThrow(new MessagingException("Failed")).when(emailService).contactUs(name, email, phone, message);

        CompletableFuture<ResponseEntity<?>> response = emailController.contactUs(name, email, phone, message);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.join().getStatusCode());
        assertTrue(Objects.requireNonNull(response.join().getBody()).toString().contains("Failed to send email"));
    }
}
