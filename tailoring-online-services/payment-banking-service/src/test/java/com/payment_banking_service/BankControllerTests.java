package com.payment_banking_service;

import com.payment_banking_service.controller.BankController;
import com.payment_banking_service.dto.BankDto;
import com.payment_banking_service.exception.BankNotFoundException;
import com.payment_banking_service.exception.UserNotFoundException;
import com.payment_banking_service.model.Bank;
import com.payment_banking_service.service.elasticsearch.BankElasticsearchService;
import com.payment_banking_service.service.jpa.BankJpaService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class BankControllerTests {

    @Mock
    private BankJpaService bankService;

    @Mock
    private BankElasticsearchService elasticsearchService;

    @InjectMocks
    private BankController bankController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBanks_Success() {
        List<Bank> banks = Arrays.asList(new Bank(), new Bank());
        Page<Bank> bankPage = new PageImpl<>(banks);
        when(bankService.getAllBanks(0, 9, "id", "asc")).thenReturn(bankPage);

        ResponseEntity<?> response = bankController.getAllBanks(0, 9, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankPage, response.getBody());
    }

    @Test
    void getAllBanks_Exception() {
        when(bankService.getAllBanks(0, 9, "id", "asc")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = bankController.getAllBanks(0, 9, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getBankById_Success() {
        Long bankId = 1L;
        Bank bank = new Bank();
        when(bankService.getBankById(bankId)).thenReturn(bank);

        ResponseEntity<?> response = bankController.getBankById(bankId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bank, response.getBody());
    }

    @Test
    void getBankById_NotFound() {
        Long bankId = 1L;
        when(bankService.getBankById(bankId)).thenThrow(new BankNotFoundException(bankId));

        ResponseEntity<?> response = bankController.getBankById(bankId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new BankNotFoundException(bankId).getMessage(), response.getBody());
    }

    @Test
    void getBankWithUser_Success() {
        Long userId = 1L;
        BankDto bankDto = new BankDto();
        when(bankService.getBankWithUser(userId)).thenReturn(bankDto);

        ResponseEntity<?> response = bankController.getBankWithUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankDto, response.getBody());
    }

    @Test
    void getBankWithUser_UserNotFound() {
        Long userId = 1L;
        when(bankService.getBankWithUser(userId)).thenThrow(new UserNotFoundException(userId.toString()));

        ResponseEntity<?> response = bankController.getBankWithUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new UserNotFoundException(userId.toString()).getMessage(), response.getBody());
    }

    @Test
    void getBankWithUser_BankNotFound() {
        Long userId = 1L;
        Long bankId = 1L;
        when(bankService.getBankWithUser(userId)).thenThrow(new BankNotFoundException(bankId));

        ResponseEntity<?> response = bankController.getBankWithUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new BankNotFoundException(bankId).getMessage(), response.getBody());
    }

    @Test
    void addBank_Success() {
        BankDto bankDto = new BankDto();
        BankDto createdBank = new BankDto();
        when(bankService.addBank(bankDto, "1")).thenReturn(createdBank);

        ResponseEntity<?> response = bankController.addBank(bankDto, "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdBank, response.getBody());
    }

    @Test
    void addBank_Exception() {
        BankDto bankDto = new BankDto();
        when(bankService.addBank(bankDto, "1")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = bankController.addBank(bankDto, "1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void updateBank_Success() {
        Long bankId = 1L;
        BankDto bankDto = new BankDto();
        BankDto updatedBank = new BankDto();
        when(bankService.updateBank(bankId, bankDto)).thenReturn(updatedBank);

        ResponseEntity<?> response = bankController.updateBank(bankId, bankDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBank, response.getBody());
    }

    @Test
    void updateBank_NotFound() {
        Long bankId = 1L;
        BankDto bankDto = new BankDto();
        when(bankService.updateBank(bankId, bankDto)).thenThrow(new BankNotFoundException(bankId));

        ResponseEntity<?> response = bankController.updateBank(bankId, bankDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new BankNotFoundException(bankId).getMessage(), response.getBody());
    }

    @Test
    void deleteBank_Success() {
        Long bankId = 1L;
        doNothing().when(bankService).deleteBank(bankId);

        ResponseEntity<?> response = bankController.deleteBank(bankId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteBank_NotFound() {
        Long bankId = 1L;
        doThrow(new BankNotFoundException(bankId)).when(bankService).deleteBank(bankId);

        ResponseEntity<?> response = bankController.deleteBank(bankId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new BankNotFoundException(bankId).getMessage(), response.getBody());
    }

    // New tests for Elasticsearch methods

    @Test
    void searchBanks_Success() {
        List<Bank> banks = Arrays.asList(new Bank(), new Bank());
        Page<Bank> bankPage = new PageImpl<>(banks);
        when(elasticsearchService.search("query", 0, 9, "id", "asc")).thenReturn(bankPage);

        ResponseEntity<?> response = bankController.search("query", 0, 9, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankPage, response.getBody());
    }

    @Test
    void searchBanks_Exception() {
        when(elasticsearchService.search("query", 0, 9, "id", "asc")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = bankController.search("query", 0, 9, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void filterBanks_Success() {
        List<Bank> banks = Arrays.asList(new Bank(), new Bank());
        Page<Bank> bankPage = new PageImpl<>(banks);
        when(elasticsearchService.filter(0, 9, "id", "asc", "1234", "2023-01-01", "2025-01-01", "123", "1")).thenReturn(bankPage);

        ResponseEntity<?> response = bankController.filter(0, 9, "id", "asc", "1234", "2023-01-01", "2025-01-01", "123", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankPage, response.getBody());
    }

    @Test
    void filterBanks_Exception() {
        when(elasticsearchService.filter(0, 9, "id", "asc", "1234", "2023-01-01", "2025-01-01", "123", "1")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = bankController.filter(0, 9, "id", "asc", "1234", "2023-01-01", "2025-01-01", "123", "1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void autocompleteBanks_Success() {
        List<String> suggestions = Arrays.asList("1234", "123");
        when(elasticsearchService.autocomplete("123")).thenReturn(suggestions);

        ResponseEntity<?> response = bankController.autocomplete("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suggestions, response.getBody());
    }

    @Test
    void autocompleteBanks_Exception() {
        when(elasticsearchService.autocomplete("123")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = bankController.autocomplete("123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
}
