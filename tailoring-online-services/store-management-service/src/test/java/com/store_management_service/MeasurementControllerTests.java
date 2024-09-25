package com.store_management_service;

import com.store_management_service.controller.MeasurementController;
import com.store_management_service.dto.MeasurementDto;
import com.store_management_service.exception.MeasurementNotFoundException;
import com.store_management_service.model.Measurement;
import com.store_management_service.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeasurementControllerTests {

    @Mock
    private MeasurementService measurementService;

    @InjectMocks
    private MeasurementController measurementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMeasurements_Success() {
        List<Measurement> measurements = Arrays.asList(new Measurement(), new Measurement());
        when(measurementService.getAllMeasurements()).thenReturn(measurements);

        ResponseEntity<?> response = measurementController.getAllMeasurements();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurements, response.getBody());
    }

    @Test
    void getAllMeasurements_Exception() {
        when(measurementService.getAllMeasurements()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = measurementController.getAllMeasurements();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getMeasurementById_Success() {
        Long id = 1L;
        Measurement measurement = new Measurement();
        when(measurementService.getMeasurementById(id)).thenReturn(measurement);

        ResponseEntity<?> response = measurementController.getMeasurementById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurement, response.getBody());
    }

    @Test
    void getMeasurementById_NotFound() {
        Long id = 1L;
        when(measurementService.getMeasurementById(id)).thenThrow(new MeasurementNotFoundException(id));

        ResponseEntity<?> response = measurementController.getMeasurementById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MeasurementNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void addMeasurement_Success() {
        MeasurementDto measurementDto = new MeasurementDto();
        MeasurementDto addedMeasurement = new MeasurementDto();
        when(measurementService.addMeasurement(measurementDto)).thenReturn(addedMeasurement);

        ResponseEntity<?> response = measurementController.addMeasurement(measurementDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(addedMeasurement, response.getBody());
    }

    @Test
    void addMeasurement_Exception() {
        MeasurementDto measurementDto = new MeasurementDto();
        when(measurementService.addMeasurement(measurementDto)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = measurementController.addMeasurement(measurementDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void updateMeasurement_Success() {
        Long id = 1L;
        MeasurementDto measurementDto = new MeasurementDto();
        MeasurementDto updatedMeasurement = new MeasurementDto();
        when(measurementService.updateMeasurement(id, measurementDto)).thenReturn(updatedMeasurement);

        ResponseEntity<?> response = measurementController.updateMeasurement(id, measurementDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMeasurement, response.getBody());
    }

    @Test
    void updateMeasurement_NotFound() {
        Long id = 1L;
        MeasurementDto measurementDto = new MeasurementDto();
        when(measurementService.updateMeasurement(id, measurementDto)).thenThrow(new MeasurementNotFoundException(id));

        ResponseEntity<?> response = measurementController.updateMeasurement(id, measurementDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MeasurementNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void deleteMeasurement_Success() {
        Long id = 1L;
        doNothing().when(measurementService).deleteMeasurement(id);

        ResponseEntity<?> response = measurementController.deleteMeasurement(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteMeasurement_NotFound() {
        Long id = 1L;
        doThrow(new MeasurementNotFoundException(id)).when(measurementService).deleteMeasurement(id);

        ResponseEntity<?> response = measurementController.deleteMeasurement(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MeasurementNotFoundException(id).getMessage(), response.getBody());
    }
}
