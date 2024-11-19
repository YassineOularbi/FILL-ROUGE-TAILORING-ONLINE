package com.cloudinary_service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class KafkaProducerException extends RuntimeException {

    private final List<String> details;

    public KafkaProducerException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

}
