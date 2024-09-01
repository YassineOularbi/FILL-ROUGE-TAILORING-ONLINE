package com.user_management_service.service;

import com.user_management_service.dto.BankDto;
import com.user_management_service.mapper.BankMapper;
import com.user_management_service.repository.BankRepository;
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

    public List<BankDto> getAllBanks() {
        var banks = bankRepository.findAll();
        return bankMapper.toDtoList(banks);
    }

    public BankDto getBankById(Long id) {
        var bank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank not found"));
        return bankMapper.toDto(bank);
    }

    public BankDto addBank(BankDto bankDto) {
        var bank = bankMapper.toEntity(bankDto);
        var savedBank = bankRepository.save(bank);
        return bankMapper.toDto(savedBank);
    }

    public BankDto updateBank(Long id, BankDto bankDto) {
        var existingBank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank not found"));
        var updatedBank = bankMapper.partialUpdate(bankDto, existingBank);
        var savedBank = bankRepository.save(updatedBank);
        return bankMapper.toDto(savedBank);
    }

    public void deleteBank(Long id) {
        var bank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank not found"));
        bankRepository.delete(bank);
    }
}
