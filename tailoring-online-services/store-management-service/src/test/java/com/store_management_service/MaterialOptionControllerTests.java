package com.store_management_service;

import com.store_management_service.controller.MaterialOptionController;
import com.store_management_service.dto.MaterialOptionDto;
import com.store_management_service.exception.CustomizableOptionNotFoundException;
import com.store_management_service.exception.MaterialNotFoundException;
import com.store_management_service.exception.MaterialOptionExistException;
import com.store_management_service.exception.MaterialOptionNotFoundException;
import com.store_management_service.model.MaterialOption;
import com.store_management_service.model.MaterialOptionKey;
import com.store_management_service.service.MaterialOptionService;
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

public class MaterialOptionControllerTests {

    @Mock
    private MaterialOptionService materialOptionService;

    @InjectMocks
    private MaterialOptionController materialOptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMaterialOptions_Success() {
        List<MaterialOptionDto> materialOptions = Arrays.asList(new MaterialOptionDto(), new MaterialOptionDto());
        when(materialOptionService.getAllMaterialOptions()).thenReturn(materialOptions);

        ResponseEntity<?> response = materialOptionController.getAllMaterialOptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(materialOptions, response.getBody());
    }

    @Test
    void getAllMaterialOptions_Exception() {
        when(materialOptionService.getAllMaterialOptions()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = materialOptionController.getAllMaterialOptions();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAllMaterialOptionsByCustomizableOption_Success() {
        Long id = 1L;
        List<MaterialOption> materialOptions = Arrays.asList(new MaterialOption(), new MaterialOption());
        when(materialOptionService.getAllMaterialOptionsByCustomizableOption(id)).thenReturn(materialOptions);

        ResponseEntity<?> response = materialOptionController.getAllMaterialOptionsByCustomizableOption(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(materialOptions, response.getBody());
    }

    @Test
    void getAllMaterialOptionsByCustomizableOption_NotFound() {
        Long id = 1L;
        when(materialOptionService.getAllMaterialOptionsByCustomizableOption(id)).thenThrow(new CustomizableOptionNotFoundException(id));

        ResponseEntity<?> response = materialOptionController.getAllMaterialOptionsByCustomizableOption(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableOptionNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void getMaterialOptionById_Success() {
        Long materialId = 1L;
        Long optionId = 2L;
        MaterialOption materialOption = new MaterialOption();
        when(materialOptionService.getMaterialOptionById(materialId, optionId)).thenReturn(materialOption);

        ResponseEntity<?> response = materialOptionController.getMaterialOptionById(String.valueOf(materialId), String.valueOf(optionId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(materialOption, response.getBody());
    }

    @Test
    void getMaterialOptionById_NotFound() {
        Long materialId = 1L;
        Long optionId = 2L;
        when(materialOptionService.getMaterialOptionById(materialId, optionId)).thenThrow(new MaterialNotFoundException(materialId));

        ResponseEntity<?> response = materialOptionController.getMaterialOptionById(String.valueOf(materialId), String.valueOf(optionId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MaterialNotFoundException(materialId).getMessage(), response.getBody());
    }

    @Test
    void addMaterialOption_Success() {
        Long materialId = 1L;
        Long optionId = 2L;
        MaterialOptionDto addedOption = new MaterialOptionDto();
        when(materialOptionService.addMaterialOption(materialId, optionId)).thenReturn(addedOption);

        ResponseEntity<?> response = materialOptionController.addMaterialOption(String.valueOf(materialId), String.valueOf(optionId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addedOption, response.getBody());
    }

    @Test
    void addMaterialOption_Exception() {
        Long materialId = 1L;
        Long optionId = 2L;
        when(materialOptionService.addMaterialOption(materialId, optionId)).thenThrow(new MaterialOptionExistException(new MaterialOptionKey(materialId, optionId)));

        ResponseEntity<?> response = materialOptionController.addMaterialOption(String.valueOf(materialId), String.valueOf(optionId));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new MaterialOptionExistException(new MaterialOptionKey(materialId, optionId)).getMessage(), response.getBody());
    }

    @Test
    void deleteMaterialOption_Success() {
        Long materialId = 1L;
        Long optionId = 2L;
        doNothing().when(materialOptionService).deleteMaterialOption(materialId, optionId);

        ResponseEntity<?> response = materialOptionController.deleteMaterialOption(String.valueOf(materialId), String.valueOf(optionId));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteMaterialOption_NotFound() {
        Long materialId = 1L;
        Long optionId = 2L;
        doThrow(new MaterialOptionNotFoundException(new MaterialOptionKey(materialId, optionId))).when(materialOptionService).deleteMaterialOption(materialId, optionId);

        ResponseEntity<?> response = materialOptionController.deleteMaterialOption(String.valueOf(materialId), String.valueOf(optionId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MaterialOptionNotFoundException(new MaterialOptionKey(materialId, optionId)).getMessage(), response.getBody());
    }
}
