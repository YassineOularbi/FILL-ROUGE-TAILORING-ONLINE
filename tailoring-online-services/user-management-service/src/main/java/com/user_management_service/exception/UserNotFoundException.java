package com.user_management_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super(String.format("Tailor not found with id: %s", id));
    }
}
