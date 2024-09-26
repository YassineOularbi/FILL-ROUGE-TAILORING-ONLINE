package com.order_management_service.exception;

public class CustomizedOptionNotFoundException extends RuntimeException {
    public CustomizedOptionNotFoundException(Long id) {
        super(String.format("Customized Option not found with ID: %s", id));
    }
}
