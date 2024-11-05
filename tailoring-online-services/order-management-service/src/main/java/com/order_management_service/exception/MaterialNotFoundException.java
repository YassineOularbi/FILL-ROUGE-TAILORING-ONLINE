package com.order_management_service.exception;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(Long id) {
        super(String.format("Material not found with ID: %s", id));
    }
}
