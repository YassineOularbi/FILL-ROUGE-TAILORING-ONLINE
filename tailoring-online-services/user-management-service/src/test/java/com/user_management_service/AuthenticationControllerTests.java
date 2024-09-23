package com.user_management_service;

import com.user_management_service.controller.AuthenticationController;
import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.exception.AuthenticationException;
import com.user_management_service.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTests {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        AuthenticationRequest request = new AuthenticationRequest("user", "password");
        var tokenResponse = new AccessTokenResponse();
        when(authenticationService.login(request)).thenReturn(tokenResponse);

        ResponseEntity<?> response = authenticationController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tokenResponse, response.getBody());
    }

    @Test
    void login_Unauthorized() {
        AuthenticationRequest request = new AuthenticationRequest("user", "wrongpassword");
        when(authenticationService.login(request)).thenThrow(new AuthenticationException("Failed to authenticate user"));

        ResponseEntity<?> response = authenticationController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(new AuthenticationException("Failed to authenticate user").getMessage(), response.getBody());
    }

    @Test
    void sendVerificationCode_Success() {
        String userId = "1";
        String email = "user@example.com";
        when(authenticationService.sendVerificationCode(userId)).thenReturn(email);

        ResponseEntity<?> response = authenticationController.sendVerificationCode(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(STR."Verification code sent to the registered email address :\{email}", response.getBody());
    }

    @Test
    void sendVerificationCode_Error() {
        String userId = "1";
        when(authenticationService.sendVerificationCode(userId)).thenThrow(new RuntimeException("Error sending code"));

        ResponseEntity<?> response = authenticationController.sendVerificationCode(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error sending code", response.getBody());
    }

    @Test
    void verifyEmail_Success() {
        String userId = "1";
        String code = "123456";
        when(authenticationService.verifyEmail(userId, code)).thenReturn("user@example.com");

        ResponseEntity<String> response = authenticationController.verifyEmail(userId, code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Email verified :user@example.com", response.getBody());
    }

    @Test
    void verifyEmail_Error() {
        String userId = "1";
        String code = "123456";
        when(authenticationService.verifyEmail(userId, code)).thenThrow(new RuntimeException("Error verifying email"));

        ResponseEntity<String> response = authenticationController.verifyEmail(userId, code);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error verifying email", response.getBody());
    }

    @Test
    void sendOtpByEmail_Success() {
        String userId = "1";
        String email = "user@example.com";
        when(authenticationService.sendOtpVerification(userId)).thenReturn(email);

        ResponseEntity<?> response = authenticationController.sendOTPByEmail(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(STR."OTP code sent to the registered email address :\{email}", response.getBody());
    }

    @Test
    void sendOtpByEmail_Error() {
        String userId = "1";
        when(authenticationService.sendOtpVerification(userId)).thenThrow(new RuntimeException("Error sending OTP"));

        ResponseEntity<?> response = authenticationController.sendOTPByEmail(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error sending OTP", response.getBody());
    }

    @Test
    void verifyOtpCode_Success() {
        String userId = "1";
        String code = "123456";
        when(authenticationService.verifyOtpCode(userId, code)).thenReturn("user@example.com");

        ResponseEntity<?> response = authenticationController.verifyOtpCode(userId, code);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"OTP verified successfully\", \"email\": \"user@example.com\"}", response.getBody());
    }

    @Test
    void verifyOtpCode_BadRequest() {
        String userId = "1";
        String code = "wrongcode";
        when(authenticationService.verifyOtpCode(userId, code)).thenThrow(new RuntimeException("Invalid OTP"));

        ResponseEntity<?> response = authenticationController.verifyOtpCode(userId, code);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid OTP", response.getBody());
    }
}
