package com.user_management_service.exception;

import com.user_management_service.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(KeycloakException.class)
    public ResponseEntity<ErrorResponse> handleKeycloakException(KeycloakException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Keycloak Error", ex.getMessage(), request, ex.getDetails());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.CONFLICT, "User Already Exists", ex.getMessage(), request, ex.getDetails());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.error("Authentication error: {}", ex.getMessage());
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Failed", ex.getMessage(), request, Collections.singletonList("Invalid username or password"));
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(RegistrationException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", ex.getMessage(), request, ex.getDetails());
    }

    @ExceptionHandler(KafkaProducerException.class)
    public ResponseEntity<ErrorResponse> handleKafkaProducerException(KafkaProducerException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Kafka Producer Error", ex.getMessage(), request, ex.getDetails());
    }

    @ExceptionHandler(KafkaConsumerException.class)
    public ResponseEntity<ErrorResponse> handleKafkaConsumerException(KafkaConsumerException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Kafka Consumer Error", ex.getMessage(), request, ex.getDetails());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        logger.error("Validation error: {}", validationErrors);
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", "Invalid input provided", request, validationErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> validationErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        logger.error("Constraint violation error: {}", validationErrors);
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Constraint Violation", "Invalid input provided", request, validationErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        String cause = (ex.getCause() != null) ? ex.getCause().toString() : "No cause available";
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request, Collections.singletonList(cause));
    }


    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String error, String message, WebRequest request, List<String> details) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                request.getDescription(false),
                details
        );
        logger.error("{}: {}", error, message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
