package com.store_management_service.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(Long id){
        super(String.format("Store not found with id: %s", id));
    }
}
