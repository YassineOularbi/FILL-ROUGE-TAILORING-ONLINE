package com.user_management_service.exception;

public class CustomerRepositoryException extends RuntimeException {
    public CustomerRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
