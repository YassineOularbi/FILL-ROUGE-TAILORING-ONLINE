package com.store_management_service;

import com.store_management_service.controller.CustomizableOptionController;
import com.store_management_service.dto.CustomizableOptionDto;
import com.store_management_service.enums.MaterialType;
import com.store_management_service.exception.CustomizableOptionNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.model.CustomizableOption;
import com.store_management_service.service.elasticsearch.CustomizableOptionElasticsearchService;
import com.store_management_service.service.jpa.CustomizableOptionJpaService;
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

class CustomizableOptionControllerTests {

    @Mock
    private CustomizableOptionJpaService customizableOptionService;

    @Mock
    private CustomizableOptionElasticsearchService elasticsearchService;

    @InjectMocks
    private CustomizableOptionController customizableOptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizableOptions_Success() {
        List<CustomizableOptionDto> dtos = Arrays.asList(new CustomizableOptionDto(), new CustomizableOptionDto());
        Page<CustomizableOptionDto> page = new PageImpl<>(dtos);
        when(customizableOptionService.getAllCustomizableOptions(0, 10, "id", "asc")).thenReturn(page);

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptions(0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void getAllCustomizableOptions_Exception() {
        when(customizableOptionService.getAllCustomizableOptions(0, 10, "id", "asc")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptions(0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAllCustomizableOptionsByThreeDModel_Success() {
        Long modelId = 1L;
        List<CustomizableOption> customizableOptions = Arrays.asList(new CustomizableOption(), new CustomizableOption());
        when(customizableOptionService.getAllCustomizableOptionsByThreeDModel(modelId)).thenReturn(customizableOptions);

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptionsByThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customizableOptions, response.getBody());
    }

    @Test
    void getAllCustomizableOptionsByThreeDModel_NotFound() {
        Long modelId = 1L;
        when(customizableOptionService.getAllCustomizableOptionsByThreeDModel(modelId))
                .thenThrow(new ThreeDModelNotFoundException(modelId));

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptionsByThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ThreeDModelNotFoundException(modelId).getMessage(), response.getBody());
    }

    @Test
    void getCustomizableOptionById_Success() {
        Long id = 1L;
        CustomizableOption customizableOption = new CustomizableOption();
        when(customizableOptionService.getCustomizableOptionById(id)).thenReturn(customizableOption);

        ResponseEntity<?> response = customizableOptionController.getCustomizableOptionById(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customizableOption, response.getBody());
    }

    @Test
    void getCustomizableOptionById_NotFound() {
        Long id = 1L;
        when(customizableOptionService.getCustomizableOptionById(id)).thenThrow(new CustomizableOptionNotFoundException(id));

        ResponseEntity<?> response = customizableOptionController.getCustomizableOptionById(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableOptionNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void addCustomizableOption_Success() {
        Long modelId = 1L;
        MaterialType type = MaterialType.ACCESSORY;
        CustomizableOptionDto dto = new CustomizableOptionDto();
        when(customizableOptionService.addCustomizableOption(modelId, type)).thenReturn(dto);

        ResponseEntity<?> response = customizableOptionController.addCustomizableOption(String.valueOf(modelId), type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void addCustomizableOption_Exception() {
        Long modelId = 1L;
        MaterialType type = MaterialType.ACCESSORY;
        when(customizableOptionService.addCustomizableOption(modelId, type)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableOptionController.addCustomizableOption(String.valueOf(modelId), type);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void deleteCustomizableOption_Success() {
        Long id = 1L;
        doNothing().when(customizableOptionService).deleteCustomizableOption(id);

        ResponseEntity<?> response = customizableOptionController.deleteCustomizableOption(String.valueOf(id));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteCustomizableOption_NotFound() {
        Long id = 1L;
        doThrow(new CustomizableOptionNotFoundException(id)).when(customizableOptionService).deleteCustomizableOption(id);

        ResponseEntity<?> response = customizableOptionController.deleteCustomizableOption(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableOptionNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void search_Success() {
        String input = "searchTerm";
        List<CustomizableOption> customizableOptions = Arrays.asList(new CustomizableOption(), new CustomizableOption());
        Page<CustomizableOption> page = new PageImpl<>(customizableOptions);
        when(elasticsearchService.search(input, 0, 10, "id", "asc")).thenReturn(page);

        ResponseEntity<?> response = customizableOptionController.search(input, 0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void search_NotFound() {
        String input = "searchTerm";
        when(elasticsearchService.search(input, 0, 10, "id", "asc")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableOptionController.search(input, 0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void filter_Success() {
        String modelIdFilter = "1";
        String typeFilter = "ACCESSORY";
        List<CustomizableOption> customizableOptions = Arrays.asList(new CustomizableOption(), new CustomizableOption());
        Page<CustomizableOption> page = new PageImpl<>(customizableOptions);
        when(elasticsearchService.filter(0, 10, "id", "asc", modelIdFilter, typeFilter)).thenReturn(page);

        ResponseEntity<?> response = customizableOptionController.filter(0, 10, "id", "asc", modelIdFilter, typeFilter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    void filter_NotFound() {
        String modelIdFilter = "1";
        String typeFilter = "ACCESSORY";
        when(elasticsearchService.filter(0, 10, "id", "asc", modelIdFilter, typeFilter)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableOptionController.filter(0, 10, "id", "asc", modelIdFilter, typeFilter);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void autocomplete_Success() {
        String input = "searchTerm";
        List<String> suggestions = Arrays.asList("suggestion1", "suggestion2");
        when(elasticsearchService.autocomplete(input)).thenReturn(suggestions);

        ResponseEntity<?> response = customizableOptionController.autocomplete(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suggestions, response.getBody());
    }

    @Test
    void autocomplete_NotFound() {
        String input = "searchTerm";
        when(elasticsearchService.autocomplete(input)).thenThrow(new RuntimeException("No suggestions found"));

        ResponseEntity<?> response = customizableOptionController.autocomplete(input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No suggestions found", response.getBody());
    }
}
