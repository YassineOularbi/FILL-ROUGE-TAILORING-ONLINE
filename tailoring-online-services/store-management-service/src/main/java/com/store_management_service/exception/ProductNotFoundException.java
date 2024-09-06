package com.store_management_service.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super(STR."Product not found with id : \{id}");
    }
}
