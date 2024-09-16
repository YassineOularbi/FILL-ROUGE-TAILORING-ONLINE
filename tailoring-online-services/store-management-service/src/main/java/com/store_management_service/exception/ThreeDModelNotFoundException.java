package com.store_management_service.exception;

public class ThreeDModelNotFoundException extends RuntimeException {
    public ThreeDModelNotFoundException(Long id) {
        super(STR."3D Model not found with id : \{id}");
    }
}
