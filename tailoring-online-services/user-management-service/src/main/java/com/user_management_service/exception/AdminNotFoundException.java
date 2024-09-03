package com.user_management_service.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String id) {
        super(STR."Admin not found with id: \{id}");
    }
}
