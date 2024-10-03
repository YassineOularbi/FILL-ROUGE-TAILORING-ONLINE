package com.payment_banking_service.service;

import com.payment_banking_service.repository.elasticsearch.BankElasticsearchRepository;
import com.payment_banking_service.repository.jpa.BankJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataIndexerService {

    private static final Logger logger = LoggerFactory.getLogger(DataIndexerService.class);

    private final BankJpaRepository bankJpaRepository;
    private final BankElasticsearchRepository bankElasticsearchRepository;

    @Async
    public void indexAllBanks() {
        logger.info("Starting indexing of all banks.");

        var banks = bankJpaRepository.findAll();
        logger.info("Found {} banks to index.", banks.size());

        if (!banks.isEmpty()) {
            bankElasticsearchRepository.saveAll(banks);
            logger.info("Successfully indexed {} banks.", banks.size());
        } else {
            logger.warn("No banks found to index.");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application is ready. Starting bank indexing.");
        indexAllBanks();
    }
}
