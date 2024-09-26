package com.user_management_service.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String id){
        super(String.format("Admin not found with id: %s", id));
    }
}
