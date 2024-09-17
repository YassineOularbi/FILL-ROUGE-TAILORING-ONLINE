package com.order_management_service.exception;

public class MeasurementNotFoundException extends RuntimeException {
    public MeasurementNotFoundException(Long id) {
        super(STR."Measurement not found with ID: \{id}");
    }
}