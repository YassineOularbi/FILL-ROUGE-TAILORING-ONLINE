package com.user_management_service.exception;

public class KeycloakServiceException extends RuntimeException {
    public KeycloakServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeycloakServiceException(String message) {
        super(message);
    }
}
