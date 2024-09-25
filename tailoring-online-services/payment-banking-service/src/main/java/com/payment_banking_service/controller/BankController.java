package com.payment_banking_service.controller;

import com.payment_banking_service.dto.BankDto;
import com.payment_banking_service.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/get-all-banks")
    public ResponseEntity<?> getAllBanks() {
        try {
            var banks = bankService.getAllBanks();
            return ResponseEntity.ok(banks);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-bank-by-id/{id}")
    public ResponseEntity<?> getBankById(@PathVariable("id") Long id) {
        try {
            var bank = bankService.getBankById(id);
            return ResponseEntity.ok(bank);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-bank-with-user/{id}")
    public ResponseEntity<?> getBankWithUser(@PathVariable("id") Long id) {
        try {
            var bank = bankService.getBankWithUser(id);
            return ResponseEntity.ok(bank);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/add-bank/{id}")
    public ResponseEntity<?> addBank(@RequestBody BankDto bankDto, @PathVariable("id") String id) {
        try {
            var addedBank = bankService.addBank(bankDto, id);
            return ResponseEntity.ok(addedBank);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-bank/{id}")
    public ResponseEntity<?> updateBank(@PathVariable("id") Long id, @RequestBody BankDto bankDto) {
        try {
            var updatedBank = bankService.updateBank(id, bankDto);
            return ResponseEntity.ok(updatedBank);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-bank/{id}")
    public ResponseEntity<?> deleteBank(@PathVariable("id") Long id) {
        try {
            bankService.deleteBank(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
