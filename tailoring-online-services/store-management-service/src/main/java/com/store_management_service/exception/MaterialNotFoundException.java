package com.store_management_service.exception;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(Long id){
        super(String.format("Material not found with id: %s", id));
    }
}
