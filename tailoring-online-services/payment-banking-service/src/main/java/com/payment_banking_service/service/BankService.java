//package com.user_management_service.service;
//
//import com.user_management_service.dto.BankDto;
//import com.user_management_service.exception.BankNotFoundException;
//import com.user_management_service.exception.UserNotFoundException;
//import com.user_management_service.mapper.BankMapper;
//import com.user_management_service.model.Bank;
//import com.user_management_service.repository.BankRepository;
//import com.user_management_service.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class BankService {
//
//    private final BankRepository bankRepository;
//    private final BankMapper bankMapper;
//    private final UserRepository userRepository;
//
//    public List<Bank> getAllBanks() {
//        return bankRepository.findAll();
//    }
//
//    public Bank getBankById(Long id) {
//        return bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
//    }
//
//    public BankDto addBank(BankDto bankDto, String id) {
//        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
//        var bank = bankMapper.toEntity(bankDto);
//        bank.setUser(user);
//        var savedBank = bankRepository.save(bank);
//        return bankMapper.toDto(savedBank);
//    }
//
//    public BankDto updateBank(Long id, BankDto bankDto) {
//        var existingBank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
//        var updatedBank = bankMapper.partialUpdate(bankDto, existingBank);
//        var savedBank = bankRepository.save(updatedBank);
//        return bankMapper.toDto(savedBank);
//    }
//
//    public void deleteBank(Long id) {
//        var bank = bankRepository.findById(id).orElseThrow(() -> new BankNotFoundException(id));
//        bankRepository.delete(bank);
//    }
//}
