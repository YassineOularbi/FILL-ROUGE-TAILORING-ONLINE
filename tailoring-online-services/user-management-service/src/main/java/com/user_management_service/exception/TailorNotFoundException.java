package com.user_management_service.exception;

public class TailorNotFoundException extends RuntimeException {
    public TailorNotFoundException(String id){
        super(STR."Tailor not found with id: \{id}");
    }
}
