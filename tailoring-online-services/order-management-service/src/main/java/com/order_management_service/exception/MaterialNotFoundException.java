package com.order_management_service.exception;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(Long id) {
        super(STR."Material not found with ID: \{id}");
    }
}
