package com.order_management_service.exception;

public class CustomizedProductNotFoundException extends RuntimeException {
    public CustomizedProductNotFoundException(Long id) {
        super(STR."Customized product with id \{id} not found !");
    }
}
