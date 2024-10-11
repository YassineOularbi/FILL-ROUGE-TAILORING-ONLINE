package com.user_management_service;

import com.user_management_service.controller.TailorController;
import com.user_management_service.dto.TailorDto;
import com.user_management_service.exception.TailorNotFoundException;
import com.user_management_service.model.Tailor;
import com.user_management_service.service.TailorService;
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

class TailorControllerTests {

    @Mock
    private TailorService tailorService;

    @InjectMocks
    private TailorController tailorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTailors_Success() {
        List<Tailor> tailors = Arrays.asList(new Tailor(), new Tailor());
        when(tailorService.getAllTailors()).thenReturn(tailors);

        ResponseEntity<?> response = tailorController.getAllTailors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tailors, response.getBody());
    }

    @Test
    void getAllTailors_Exception() {
        when(tailorService.getAllTailors()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = tailorController.getAllTailors();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getTailorById_Success() {
        String id = "1";
        Tailor tailor = new Tailor();
        when(tailorService.getTailorById(id)).thenReturn(tailor);

        ResponseEntity<?> response = tailorController.getTailorById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tailor, response.getBody());
    }

    @Test
    void getTailorById_NotFound() {
        String id = "1";
        when(tailorService.getTailorById(id)).thenThrow(new TailorNotFoundException(id));

        ResponseEntity<?> response = tailorController.getTailorById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new TailorNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void updateTailor_Success() {
        String id = "1";
        TailorDto tailorDto = new TailorDto();
        when(tailorService.updateTailor(id, tailorDto)).thenReturn(tailorDto);

        ResponseEntity<?> response = tailorController.updateTailor(id, tailorDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tailorDto, response.getBody());
    }

    @Test
    void updateTailor_NotFound() {
        String id = "1";
        TailorDto tailorDto = new TailorDto();
        when(tailorService.updateTailor(id, tailorDto)).thenThrow(new TailorNotFoundException(id));

        ResponseEntity<?> response = tailorController.updateTailor(id, tailorDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new TailorNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void deleteTailor_Success() {
        String id = "1";
        doNothing().when(tailorService).deleteTailor(id);

        ResponseEntity<?> response = tailorController.deleteTailor(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteTailor_NotFound() {
        String id = "1";
        doThrow(new TailorNotFoundException(id)).when(tailorService).deleteTailor(id);

        ResponseEntity<?> response = tailorController.deleteTailor(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new TailorNotFoundException(id).getMessage(), response.getBody());
    }
}
