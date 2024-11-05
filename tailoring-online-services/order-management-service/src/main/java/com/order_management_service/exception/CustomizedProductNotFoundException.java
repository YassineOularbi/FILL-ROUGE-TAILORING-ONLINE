package com.order_management_service.exception;

public class CustomizedProductNotFoundException extends RuntimeException {
    public CustomizedProductNotFoundException(Long id) {
        super(String.format("Customized product with id %s not found!", id));
    }
}
