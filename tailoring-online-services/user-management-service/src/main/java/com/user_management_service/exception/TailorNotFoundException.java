package com.user_management_service.exception;

public class TailorNotFoundException extends RuntimeException {
    public TailorNotFoundException(String id){
        super(String.format("Tailor not found with id: %s", id));
    }
}
