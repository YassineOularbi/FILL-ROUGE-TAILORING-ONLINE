package com.cloudinary_service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class KafkaConsumerException extends RuntimeException {

    private final List<String> details;

    public KafkaConsumerException(String message, List<String> details) {
        super(message);
        this.details = details;
    }

}
