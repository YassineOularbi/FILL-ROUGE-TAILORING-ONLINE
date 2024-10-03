package com.store_management_service;

import com.store_management_service.controller.CustomizableMeasurementController;
import com.store_management_service.dto.CustomizableMeasurementDto;
import com.store_management_service.exception.CustomizableMeasurementNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.model.CustomizableMeasurement;
import com.store_management_service.model.CustomizableMeasurementKey;
import com.store_management_service.service.elasticsearch.CustomizableMeasurementElasticsearchService;
import com.store_management_service.service.jpa.CustomizableMeasurementJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomizableMeasurementControllerTests {

    @Mock
    private CustomizableMeasurementJpaService customizableMeasurementService;

    @Mock
    private CustomizableMeasurementElasticsearchService elasticsearchService;

    @InjectMocks
    private CustomizableMeasurementController customizableMeasurementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for JPA Service Methods

    @Test
    void getAllCustomizableMeasurement_Success() {
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";

        List<CustomizableMeasurementDto> dtos = Arrays.asList(new CustomizableMeasurementDto(), new CustomizableMeasurementDto());
        Page<CustomizableMeasurementDto> pageResult = new PageImpl<>(dtos);

        when(customizableMeasurementService.getAllCustomizableMeasurement(page, size, sortField, sortDirection)).thenReturn(pageResult);

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurement(page, size, sortField, sortDirection);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResult, response.getBody());
    }

    @Test
    void getAllCustomizableMeasurement_Exception() {
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";

        when(customizableMeasurementService.getAllCustomizableMeasurement(page, size, sortField, sortDirection))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurement(page, size, sortField, sortDirection);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAllCustomizableMeasurementByThreeDModel_Success() {
        Long modelId = 1L;
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";

        List<CustomizableMeasurement> measurements = Arrays.asList(new CustomizableMeasurement(), new CustomizableMeasurement());
        Page<CustomizableMeasurement> pageResult = new PageImpl<>(measurements);

        when(customizableMeasurementService.getAllCustomizableMeasurementByThreeDModel(modelId, page, size, sortField, sortDirection))
                .thenReturn(pageResult);

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurementByThreeDModel(
                String.valueOf(modelId), page, size, sortField, sortDirection);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResult, response.getBody());
    }

    @Test
    void getAllCustomizableMeasurementByThreeDModel_NotFound() {
        Long modelId = 1L;
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";

        when(customizableMeasurementService.getAllCustomizableMeasurementByThreeDModel(modelId, page, size, sortField, sortDirection))
                .thenThrow(new ThreeDModelNotFoundException(modelId));

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurementByThreeDModel(
                String.valueOf(modelId), page, size, sortField, sortDirection);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ThreeDModelNotFoundException(modelId).getMessage(), response.getBody());
    }

    @Test
    void getCustomizableMeasurementById_Success() {
        Long modelId = 1L;
        Long measurementId = 1L;
        CustomizableMeasurement measurement = new CustomizableMeasurement();

        when(customizableMeasurementService.getCustomizableMeasurementById(modelId, measurementId))
                .thenReturn(measurement);

        ResponseEntity<?> response = customizableMeasurementController.getCustomizableMeasurementById(
                String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurement, response.getBody());
    }

    @Test
    void getCustomizableMeasurementById_NotFound() {
        Long modelId = 1L;
        Long measurementId = 1L;
        CustomizableMeasurementKey key = new CustomizableMeasurementKey(modelId, measurementId);

        when(customizableMeasurementService.getCustomizableMeasurementById(modelId, measurementId))
                .thenThrow(new CustomizableMeasurementNotFoundException(key));

        ResponseEntity<?> response = customizableMeasurementController.getCustomizableMeasurementById(
                String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableMeasurementNotFoundException(key).getMessage(), response.getBody());
    }

    @Test
    void addCustomizableMeasurement_Success() {
        Long modelId = 1L;
        Long measurementId = 1L;
        CustomizableMeasurementDto dto = new CustomizableMeasurementDto();

        when(customizableMeasurementService.addCustomizableMeasurement(modelId, measurementId))
                .thenReturn(dto);

        ResponseEntity<?> response = customizableMeasurementController.addCustomizableMeasurement(
                String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void addCustomizableMeasurement_Exception() {
        Long modelId = 1L;
        Long measurementId = 1L;

        when(customizableMeasurementService.addCustomizableMeasurement(modelId, measurementId))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableMeasurementController.addCustomizableMeasurement(
                String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void deleteCustomizableMeasurement_Success() {
        Long modelId = 1L;
        Long measurementId = 1L;

        doNothing().when(customizableMeasurementService).deleteCustomizableMeasurement(modelId, measurementId);

        ResponseEntity<?> response = customizableMeasurementController.deleteCustomizableMeasurement(
                String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteCustomizableMeasurement_NotFound() {
        Long modelId = 1L;
        Long measurementId = 1L;
        CustomizableMeasurementKey key = new CustomizableMeasurementKey(modelId, measurementId);

        doThrow(new CustomizableMeasurementNotFoundException(key))
                .when(customizableMeasurementService).deleteCustomizableMeasurement(modelId, measurementId);

        ResponseEntity<?> response = customizableMeasurementController.deleteCustomizableMeasurement(
                String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableMeasurementNotFoundException(key).getMessage(), response.getBody());
    }

    // Tests for Elasticsearch Service Methods

    @Test
    void search_Success() {
        String input = "query";
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";

        List<CustomizableMeasurement> measurements = Arrays.asList(new CustomizableMeasurement(), new CustomizableMeasurement());
        Page<CustomizableMeasurement> pageResult = new PageImpl<>(measurements);

        when(elasticsearchService.search(input, page, size, sortField, sortDirection)).thenReturn(pageResult);

        ResponseEntity<?> response = customizableMeasurementController.search(input, page, size, sortField, sortDirection);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResult, response.getBody());
    }

    @Test
    void search_Exception() {
        String input = "query";
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";

        when(elasticsearchService.search(input, page, size, sortField, sortDirection))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableMeasurementController.search(input, page, size, sortField, sortDirection);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void filter_Success() {
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";
        String modelIdFilter = "1";
        String measurementNameFilter = "length";

        List<CustomizableMeasurement> measurements = Arrays.asList(new CustomizableMeasurement(), new CustomizableMeasurement());
        Page<CustomizableMeasurement> pageResult = new PageImpl<>(measurements);

        when(elasticsearchService.filter(page, size, sortField, sortDirection, modelIdFilter, measurementNameFilter))
                .thenReturn(pageResult);

        ResponseEntity<?> response = customizableMeasurementController.filter(page, size, sortField, sortDirection, modelIdFilter, measurementNameFilter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResult, response.getBody());
    }

    @Test
    void filter_Exception() {
        int page = 0;
        int size = 10;
        String sortField = "id.modelId";
        String sortDirection = "asc";
        String modelIdFilter = "1";
        String measurementNameFilter = "length";

        when(elasticsearchService.filter(page, size, sortField, sortDirection, modelIdFilter, measurementNameFilter))
                .thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableMeasurementController.filter(page, size, sortField, sortDirection, modelIdFilter, measurementNameFilter);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void autocomplete_Success() {
        String input = "len";
        List<String> suggestions = Arrays.asList("1", "length");

        when(elasticsearchService.autocomplete(input)).thenReturn(suggestions);

        ResponseEntity<?> response = customizableMeasurementController.autocomplete(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suggestions, response.getBody());
    }

    @Test
    void autocomplete_Exception() {
        String input = "len";

        when(elasticsearchService.autocomplete(input)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableMeasurementController.autocomplete(input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
}
