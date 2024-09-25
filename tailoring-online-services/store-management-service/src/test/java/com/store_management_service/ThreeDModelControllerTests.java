package com.store_management_service;

import com.store_management_service.controller.ThreeDModelController;
import com.store_management_service.dto.ThreeDModelDto;
import com.store_management_service.exception.ProductNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.model.ThreeDModel;
import com.store_management_service.service.ThreeDModelService;
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

class ThreeDModelControllerTests {

    @Mock
    private ThreeDModelService threeDModelService;

    @InjectMocks
    private ThreeDModelController threeDModelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllThreeDModel_Success() {
        List<ThreeDModelDto> models = Arrays.asList(new ThreeDModelDto(), new ThreeDModelDto());
        when(threeDModelService.getAllThreeDModel()).thenReturn(models);

        ResponseEntity<?> response = threeDModelController.getAllThreeDModel();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(models, response.getBody());
    }

    @Test
    void getAllThreeDModel_Exception() {
        when(threeDModelService.getAllThreeDModel()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = threeDModelController.getAllThreeDModel();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getThreeDModelByProduct_Success() {
        Long productId = 1L;
        ThreeDModel model = new ThreeDModel();
        when(threeDModelService.getThreeDModelByProduct(productId)).thenReturn(model);

        ResponseEntity<?> response = threeDModelController.getThreeDModelByProduct(String.valueOf(productId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(model, response.getBody());
    }

    @Test
    void getThreeDModelByProduct_NotFound() {
        Long productId = 1L;
        when(threeDModelService.getThreeDModelByProduct(productId)).thenThrow(new ProductNotFoundException(productId));

        ResponseEntity<?> response = threeDModelController.getThreeDModelByProduct(String.valueOf(productId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ProductNotFoundException(productId).getMessage(), response.getBody());
    }

    @Test
    void getThreeDModelById_Success() {
        Long modelId = 1L;
        ThreeDModel model = new ThreeDModel();
        when(threeDModelService.getThreeDModelById(modelId)).thenReturn(model);

        ResponseEntity<?> response = threeDModelController.getThreeDModelById(String.valueOf(modelId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(model, response.getBody());
    }

    @Test
    void getThreeDModelById_NotFound() {
        Long modelId = 1L;
        when(threeDModelService.getThreeDModelById(modelId)).thenThrow(new ThreeDModelNotFoundException(modelId));

        ResponseEntity<?> response = threeDModelController.getThreeDModelById(String.valueOf(modelId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ThreeDModelNotFoundException(modelId).getMessage(), response.getBody());
    }

    @Test
    void addThreeDModel_Success() {
        Long productId = 1L;
        ThreeDModelDto addedModel = new ThreeDModelDto();
        when(threeDModelService.addThreeDModel(productId)).thenReturn(addedModel);

        ResponseEntity<?> response = threeDModelController.addThreeDModel(String.valueOf(productId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addedModel, response.getBody());
    }

    @Test
    void addThreeDModel_Exception() {
        Long productId = 1L;
        when(threeDModelService.addThreeDModel(productId)).thenThrow(new ProductNotFoundException(productId));

        ResponseEntity<?> response = threeDModelController.addThreeDModel(String.valueOf(productId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new ProductNotFoundException(productId).getMessage(), response.getBody());
    }

    @Test
    void deleteThreeDModel_Success() {
        Long modelId = 1L;
        doNothing().when(threeDModelService).deleteThreeDModel(modelId);

        ResponseEntity<?> response = threeDModelController.deleteThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteThreeDModel_NotFound() {
        Long modelId = 1L;
        doThrow(new ThreeDModelNotFoundException(modelId)).when(threeDModelService).deleteThreeDModel(modelId);

        ResponseEntity<?> response = threeDModelController.deleteThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ThreeDModelNotFoundException(modelId).getMessage(), response.getBody());
    }
}
