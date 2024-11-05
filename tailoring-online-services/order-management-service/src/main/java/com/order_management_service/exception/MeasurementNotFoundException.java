package com.order_management_service.exception;

public class MeasurementNotFoundException extends RuntimeException {
    public MeasurementNotFoundException(Long id) {
        super(String.format("Measurement not found with ID: %s", id));
    }
}
