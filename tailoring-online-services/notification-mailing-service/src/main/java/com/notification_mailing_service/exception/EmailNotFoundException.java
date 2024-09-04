package com.notification_mailing_service.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("Email not found !");
    }
}
