package com.user_management_service.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String id){
        super(STR."Customer not found with id: \{id}");
    }
}
