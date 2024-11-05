package com.store_management_service;

import com.store_management_service.controller.StoreController;
import com.store_management_service.dto.StoreDto;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.exception.TailorNotFoundException;
import com.store_management_service.model.Store;
import com.store_management_service.service.jpa.StoreJpaService;
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

class StoreControllerTests {

    @Mock
    private StoreJpaService storeService;

    @InjectMocks
    private StoreController storeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void getAllStores_Success() {
//        List<Store> stores = Arrays.asList(new Store(), new Store());
//        when(storeService.getAllStores()).thenReturn(stores);
//
//        ResponseEntity<?> response = storeController.getAllStores();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(stores, response.getBody());
//    }
//
//    @Test
//    void getAllStores_Exception() {
//        when(storeService.getAllStores()).thenThrow(new RuntimeException("Error"));
//
//        ResponseEntity<?> response = storeController.getAllStores();
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Error", response.getBody());
//    }

    @Test
    void getStoreById_Success() {
        Long id = 1L;
        Store store = new Store();
        when(storeService.getStoreById(id)).thenReturn(store);

        ResponseEntity<?> response = storeController.getStoreById(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(store, response.getBody());
    }

    @Test
    void getStoreById_NotFound() {
        Long id = 1L;
        when(storeService.getStoreById(id)).thenThrow(new StoreNotFoundException(id));

        ResponseEntity<?> response = storeController.getStoreById(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new StoreNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void getStoreWithDetails_Success() {
        Long id = 1L;
        StoreDto storeDto = new StoreDto();
        when(storeService.getStoreWithTailor(id)).thenReturn(storeDto);

        ResponseEntity<?> response = storeController.getStoreWithDetails(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(storeDto, response.getBody());
    }

    @Test
    void getStoreWithDetails_NotFound() {
        Long id = 1L;
        when(storeService.getStoreWithTailor(id)).thenThrow(new StoreNotFoundException(id));

        ResponseEntity<?> response = storeController.getStoreWithDetails(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new StoreNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void addStore_Success() {
        String tailorId = "tailorId";
        StoreDto storeDto = new StoreDto();
        StoreDto addedStore = new StoreDto();
        when(storeService.addStore(storeDto, tailorId)).thenReturn(addedStore);

        ResponseEntity<?> response = storeController.addStore(storeDto, tailorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addedStore, response.getBody());
    }

    @Test
    void addStore_Exception() {
        String tailorId = "tailorId";
        StoreDto storeDto = new StoreDto();
        when(storeService.addStore(storeDto, tailorId)).thenThrow(new TailorNotFoundException(tailorId));

        ResponseEntity<?> response = storeController.addStore(storeDto, tailorId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new TailorNotFoundException(tailorId).getMessage(), response.getBody());
    }

    @Test
    void updateStore_Success() {
        Long id = 1L;
        StoreDto storeDto = new StoreDto();
        StoreDto updatedStore = new StoreDto();
        when(storeService.updateStore(id, storeDto)).thenReturn(updatedStore);

        ResponseEntity<?> response = storeController.updateStore(id, storeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedStore, response.getBody());
    }

    @Test
    void updateStore_NotFound() {
        Long id = 1L;
        StoreDto storeDto = new StoreDto();
        when(storeService.updateStore(id, storeDto)).thenThrow(new StoreNotFoundException(id));

        ResponseEntity<?> response = storeController.updateStore(id, storeDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new StoreNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void deleteStore_Success() {
        Long id = 1L;
        doNothing().when(storeService).deleteStore(id);

        ResponseEntity<?> response = storeController.deleteStore(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteStore_NotFound() {
        Long id = 1L;
        doThrow(new StoreNotFoundException(id)).when(storeService).deleteStore(id);

        ResponseEntity<?> response = storeController.deleteStore(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new StoreNotFoundException(id).getMessage(), response.getBody());
    }
}
