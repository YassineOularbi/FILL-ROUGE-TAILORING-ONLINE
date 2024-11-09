package com.user_management_service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    private final List<String> details;

    public UserAlreadyExistsException(String message, List<String> details) {
        super(message);
        this.details = details;
    }
}
