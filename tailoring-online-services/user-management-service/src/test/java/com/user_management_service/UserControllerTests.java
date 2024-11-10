//package com.user_management_service;
//
//import com.user_management_service.controller.UserController;
//import com.user_management_service.exception.UserNotFoundException;
//import com.user_management_service.model.User;
//import com.user_management_service.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserControllerTests {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getAllUsers_Success() {
//        List<User> users = Arrays.asList(new User(), new User());
//        when(userService.getAllUsers()).thenReturn(users);
//
//        ResponseEntity<?> response = userController.getAllUsers();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(users, response.getBody());
//    }
//
//    @Test
//    void getAllUsers_Exception() {
//        when(userService.getAllUsers()).thenThrow(new RuntimeException("Error"));
//
//        ResponseEntity<?> response = userController.getAllUsers();
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Error", response.getBody());
//    }
//
//    @Test
//    void getUserById_Success() {
//        String id = "1";
//        User user = new User();
//        when(userService.getUserById(id)).thenReturn(user);
//
//        ResponseEntity<?> response = userController.getUserById(id);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//    }
//
//    @Test
//    void getUserById_NotFound() {
//        String id = "1";
//        when(userService.getUserById(id)).thenThrow(new UserNotFoundException(id));
//
//        ResponseEntity<?> response = userController.getUserById(id);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(new UserNotFoundException(id).getMessage(), response.getBody());
//    }
//}
