package com.payment_banking_service.service;

import com.payment_banking_service.client.UserManagementClient;
import com.payment_banking_service.dto.BankDto;
import com.payment_banking_service.exception.BankNotFoundException;
import com.payment_banking_service.exception.UserNotFoundException;
import com.payment_banking_service.mapper.BankMapper;
import com.payment_banking_service.model.Bank;
import com.payment_banking_service.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;
    private final UserManagementClient userManagementClient;

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Bank getBankById(Long id) {
        return bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
    }

    public BankDto getBankWithUser(Long id){
        var bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        var user = userManagementClient.getUserById(bank.getUserId()).orElseThrow(() -> new UserNotFoundException(bank.getUserId()));
        var mappedBank = bankMapper.toDto(bank);
        mappedBank.setUser(user);
        return mappedBank;
    }

    public BankDto addBank(BankDto bankDto, String id) {
        var user = userManagementClient.getUserById(id).orElseThrow(() -> new UserNotFoundException(id));
        var bank = bankRepository.findByUserId(id);
        if (bank.isPresent()){
            return updateBank(bank.get().getId(), bankDto);
        } else {
            var mappedBank = bankMapper.toEntity(bankDto);
            mappedBank.setUserId(user.getId());
            var savedBank = bankRepository.save(mappedBank);
            return bankMapper.toDto(savedBank);
        }
    }

    public BankDto updateBank(Long id, BankDto bankDto) {
        var existingBank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        var updatedBank = bankMapper.partialUpdate(bankDto, existingBank);
        var savedBank = bankRepository.save(updatedBank);
        return bankMapper.toDto(savedBank);
    }

    public void deleteBank(Long id) {
        var bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
        bankRepository.delete(bank);
    }
}
