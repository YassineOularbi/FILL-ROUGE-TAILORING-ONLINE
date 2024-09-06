package com.store_management_service.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(Long id){
        super(STR."Store not found with id :\{id}");
    }
}
