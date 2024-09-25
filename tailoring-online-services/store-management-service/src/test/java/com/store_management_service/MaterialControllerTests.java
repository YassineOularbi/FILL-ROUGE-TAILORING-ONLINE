package com.store_management_service;

import com.store_management_service.controller.MaterialController;
import com.store_management_service.dto.MaterialDto;
import com.store_management_service.exception.MaterialNotFoundException;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.model.Material;
import com.store_management_service.service.MaterialService;
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

class MaterialControllerTests {

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMaterials_Success() {
        List<Material> materials = Arrays.asList(new Material(), new Material());
        when(materialService.getAllMaterials()).thenReturn(materials);

        ResponseEntity<?> response = materialController.getAllMaterials();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(materials, response.getBody());
    }

    @Test
    void getAllMaterials_Exception() {
        when(materialService.getAllMaterials()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = materialController.getAllMaterials();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAllMaterialsByStore_Success() {
        Long storeId = 1L;
        List<Material> materials = Arrays.asList(new Material(), new Material());
        when(materialService.getAllMaterialsByStore(storeId)).thenReturn(materials);

        ResponseEntity<?> response = materialController.getAllMaterialsByStore(String.valueOf(storeId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(materials, response.getBody());
    }

    @Test
    void getAllMaterialsByStore_NotFound() {
        Long storeId = 1L;
        when(materialService.getAllMaterialsByStore(storeId)).thenThrow(new StoreNotFoundException(storeId));

        ResponseEntity<?> response = materialController.getAllMaterialsByStore(String.valueOf(storeId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new StoreNotFoundException(storeId).getMessage(), response.getBody());
    }

    @Test
    void getMaterialById_Success() {
        Long id = 1L;
        Material material = new Material();
        when(materialService.getMaterialById(id)).thenReturn(material);

        ResponseEntity<?> response = materialController.getMaterialById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(material, response.getBody());
    }

    @Test
    void getMaterialById_NotFound() {
        Long id = 1L;
        when(materialService.getMaterialById(id)).thenThrow(new MaterialNotFoundException(id));

        ResponseEntity<?> response = materialController.getMaterialById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MaterialNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void addMaterial_Success() {
        Long storeId = 1L;
        MaterialDto materialDto = new MaterialDto();
        MaterialDto addedMaterial = new MaterialDto();
        when(materialService.addMaterial(materialDto, storeId)).thenReturn(addedMaterial);

        ResponseEntity<?> response = materialController.addMaterial(materialDto, storeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addedMaterial, response.getBody());
    }

    @Test
    void addMaterial_Exception() {
        Long storeId = 1L;
        MaterialDto materialDto = new MaterialDto();
        when(materialService.addMaterial(materialDto, storeId)).thenThrow(new StoreNotFoundException(storeId));

        ResponseEntity<?> response = materialController.addMaterial(materialDto, storeId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new StoreNotFoundException(storeId).getMessage(), response.getBody());
    }

    @Test
    void updateMaterial_Success() {
        Long id = 1L;
        MaterialDto materialDto = new MaterialDto();
        MaterialDto updatedMaterial = new MaterialDto();
        when(materialService.updateMaterial(id, materialDto)).thenReturn(updatedMaterial);

        ResponseEntity<?> response = materialController.updateMaterial(id, materialDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMaterial, response.getBody());
    }

    @Test
    void updateMaterial_NotFound() {
        Long id = 1L;
        MaterialDto materialDto = new MaterialDto();
        when(materialService.updateMaterial(id, materialDto)).thenThrow(new MaterialNotFoundException(id));

        ResponseEntity<?> response = materialController.updateMaterial(id, materialDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MaterialNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void deleteMaterial_Success() {
        Long id = 1L;
        doNothing().when(materialService).deleteMaterial(id);

        ResponseEntity<?> response = materialController.deleteMaterial(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteMaterial_NotFound() {
        Long id = 1L;
        doThrow(new MaterialNotFoundException(id)).when(materialService).deleteMaterial(id);

        ResponseEntity<?> response = materialController.deleteMaterial(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new MaterialNotFoundException(id).getMessage(), response.getBody());
    }
}
