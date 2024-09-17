package com.order_management_service.exception;

public class CustomizedOptionNotFoundException extends RuntimeException {
    public CustomizedOptionNotFoundException(Long id) {
        super(STR."Customized Option not found with ID: \{id}");
    }
}
