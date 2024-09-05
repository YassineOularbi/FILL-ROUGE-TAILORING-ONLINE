package com.payment_banking_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super(STR."User not found with id: \{id}");
    }
}
