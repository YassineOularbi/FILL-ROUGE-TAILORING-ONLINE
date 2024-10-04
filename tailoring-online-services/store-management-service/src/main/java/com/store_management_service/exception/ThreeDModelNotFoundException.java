package com.store_management_service.exception;

public class ThreeDModelNotFoundException extends RuntimeException {
    public ThreeDModelNotFoundException(Long id) {
        super(String.format("3D Model not found with id: %s", id));
    }
}
