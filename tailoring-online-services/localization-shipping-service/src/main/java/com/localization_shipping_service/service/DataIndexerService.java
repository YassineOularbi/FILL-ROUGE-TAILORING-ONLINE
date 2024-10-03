package com.localization_shipping_service.service;

import com.localization_shipping_service.model.Address;
import com.localization_shipping_service.repository.elasticsearch.AddressElasticsearchRepository;
import com.localization_shipping_service.repository.jpa.AddressJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataIndexerService {

    private static final Logger logger = LoggerFactory.getLogger(DataIndexerService.class);

    private final AddressJpaRepository addressJpaRepository;
    private final AddressElasticsearchRepository addressElasticsearchRepository;

    @Async
    public void indexAllAddresses() {
        logger.info("Starting indexing of all addresses.");

        List<Address> addresses = addressJpaRepository.findAll();
        logger.info("Found {} addresses to index.", addresses.size());

        if (!addresses.isEmpty()) {
            addressElasticsearchRepository.saveAll(addresses);
            logger.info("Successfully indexed {} addresses.", addresses.size());
        } else {
            logger.warn("No addresses found to index.");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application is ready. Starting address indexing.");
        indexAllAddresses();
    }
}
