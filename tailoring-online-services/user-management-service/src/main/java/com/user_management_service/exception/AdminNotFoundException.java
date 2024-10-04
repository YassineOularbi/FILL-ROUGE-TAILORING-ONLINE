package com.user_management_service.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String id) {
        super(String.format("Admin not found with id: %s", id));
    }
}
