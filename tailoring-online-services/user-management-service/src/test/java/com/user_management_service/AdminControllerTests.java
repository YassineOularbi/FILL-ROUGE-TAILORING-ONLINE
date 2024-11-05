package com.user_management_service;

import com.user_management_service.controller.AdminController;
import com.user_management_service.dto.AdminDto;
import com.user_management_service.exception.AdminNotFoundException;
import com.user_management_service.model.Admin;
import com.user_management_service.service.AdminService;
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

class AdminControllerTests {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAdmins_Success() {
        List<Admin> admins = Arrays.asList(new Admin(), new Admin());
        when(adminService.getAllAdmins()).thenReturn(admins);

        ResponseEntity<?> response = adminController.getAllAdmins();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admins, response.getBody());
    }

    @Test
    void getAllAdmins_Exception() {
        when(adminService.getAllAdmins()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = adminController.getAllAdmins();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAdminById_Success() {
        String id = "1";
        Admin admin = new Admin();
        when(adminService.getAdminById(id)).thenReturn(admin);

        ResponseEntity<?> response = adminController.getAdminById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(admin, response.getBody());
    }

    @Test
    void getAdminById_NotFound() {
        String id = "1";
        when(adminService.getAdminById(id)).thenThrow(new AdminNotFoundException(id));

        ResponseEntity<?> response = adminController.getAdminById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new AdminNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void updateAdmin_Success() {
        String id = "1";
        AdminDto adminDto = new AdminDto();
        when(adminService.updateAdmin(id, adminDto)).thenReturn(adminDto);

        ResponseEntity<?> response = adminController.updateAdmin(id, adminDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adminDto, response.getBody());
    }

    @Test
    void updateAdmin_NotFound() {
        String id = "1";
        AdminDto adminDto = new AdminDto();
        when(adminService.updateAdmin(id, adminDto)).thenThrow(new AdminNotFoundException(id));

        ResponseEntity<?> response = adminController.updateAdmin(id, adminDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new AdminNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void deleteAdmin_Success() {
        String id = "1";
        doNothing().when(adminService).deleteAdmin(id);

        ResponseEntity<?> response = adminController.deleteAdmin(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteAdmin_NotFound() {
        String id = "1";
        doThrow(new AdminNotFoundException(id)).when(adminService).deleteAdmin(id);

        ResponseEntity<?> response = adminController.deleteAdmin(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new AdminNotFoundException(id).getMessage(), response.getBody());
    }
}
