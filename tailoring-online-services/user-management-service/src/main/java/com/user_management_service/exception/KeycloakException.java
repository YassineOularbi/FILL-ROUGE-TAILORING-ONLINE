package com.user_management_service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class KeycloakException extends RuntimeException {

    private final List<String> details;

    public KeycloakException(String message, List<String> details) {
        super(message);
        this.details = details;
    }
}
