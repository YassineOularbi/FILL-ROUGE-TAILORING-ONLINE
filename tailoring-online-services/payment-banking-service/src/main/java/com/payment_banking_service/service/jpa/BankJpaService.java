package com.payment_banking_service.service.jpa;

import com.payment_banking_service.client.UserManagementClient;
import com.payment_banking_service.dto.BankDto;
import com.payment_banking_service.exception.BankNotFoundException;
import com.payment_banking_service.exception.UserNotFoundException;
import com.payment_banking_service.mapper.BankMapper;
import com.payment_banking_service.model.Bank;
import com.payment_banking_service.repository.jpa.BankJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BankJpaService {

    private static final Logger logger = LoggerFactory.getLogger(BankJpaService.class);
    private final BankJpaRepository bankRepository;
    private final BankMapper bankMapper;
    private final UserManagementClient userManagementClient;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<Bank> getAllBanks(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all banks with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Bank> banks = bankRepository.findAll(pageable);

        logger.info("Fetched {} banks", banks.getTotalElements());
        return banks;
    }

    public Bank getBankById(Long id) {
        logger.info("Fetching bank with ID: {}", id);
        return bankRepository.findById(id).orElseThrow(() -> {
            logger.error("Bank not found with ID: {}", id);
            return new BankNotFoundException(id);
        });
    }

    public BankDto getBankWithUser(Long id) {
        logger.info("Fetching bank with user details for ID: {}", id);
        var bank = bankRepository.findById(id).orElseThrow(() -> {
            logger.error("Bank not found with ID: {}", id);
            return new BankNotFoundException(id);
        });
        var user = userManagementClient.getUserById(bank.getUserId()).orElseThrow(() -> {
            logger.error("User not found with ID: {}", bank.getUserId());
            return new UserNotFoundException(bank.getUserId());
        });
        var mappedBank = bankMapper.toDto(bank);
        mappedBank.setUser(user);
        logger.info("Fetched bank with user details successfully for ID: {}", id);
        return mappedBank;
    }

    public BankDto addBank(BankDto bankDto, String userId) {
        logger.info("Adding bank for user ID: {}", userId);
        var user = userManagementClient.getUserById(userId).orElseThrow(() -> {
            logger.error("User not found with ID: {}", userId);
            return new UserNotFoundException(userId);
        });
        var bank = bankRepository.findByUserId(userId);
        if (bank.isPresent()) {
            return updateBank(bank.get().getId(), bankDto);
        } else {
            var mappedBank = bankMapper.toEntity(bankDto);
            mappedBank.setUserId(user.getId());
            var savedBank = bankRepository.save(mappedBank);
            logger.info("Bank added successfully for user ID: {}", userId);
            return bankMapper.toDto(savedBank);
        }
    }

    public BankDto updateBank(Long id, BankDto bankDto) {
        logger.info("Updating bank with ID: {}", id);
        var existingBank = bankRepository.findById(id).orElseThrow(() -> {
            logger.error("Bank not found with ID: {}", id);
            return new BankNotFoundException(id);
        });
        var updatedBank = bankMapper.partialUpdate(bankDto, existingBank);
        var savedBank = bankRepository.save(updatedBank);
        logger.info("Bank updated successfully with ID: {}", id);
        return bankMapper.toDto(savedBank);
    }

    public void deleteBank(Long id) {
        logger.info("Deleting bank with ID: {}", id);
        var bank = bankRepository.findById(id).orElseThrow(() -> {
            logger.error("Bank not found with ID: {}", id);
            return new BankNotFoundException(id);
        });
        bankRepository.delete(bank);
        logger.info("Bank deleted successfully with ID: {}", id);
    }
}
