package com.order_management_service.service;

import com.order_management_service.repository.elasticsearch.CustomizedMeasurementElasticsearchRepository;
import com.order_management_service.repository.elasticsearch.CustomizedOptionElasticsearchRepository;
import com.order_management_service.repository.elasticsearch.CustomizedProductElasticsearchRepository;
import com.order_management_service.repository.jpa.CustomizedMeasurementJpaRepository;
import com.order_management_service.repository.jpa.CustomizedOptionJpaRepository;
import com.order_management_service.repository.jpa.CustomizedProductJpaRepository;
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

    private final CustomizedProductJpaRepository customizedProductJpaRepository;
    private final CustomizedOptionJpaRepository customizedOptionJpaRepository;
    private final CustomizedMeasurementJpaRepository customizedMeasurementJpaRepository;
    private final CustomizedProductElasticsearchRepository customizedProductElasticsearchRepository;
    private final CustomizedOptionElasticsearchRepository customizedOptionElasticsearchRepository;
    private final CustomizedMeasurementElasticsearchRepository customizedMeasurementElasticsearchRepository;

    @Async
    public void indexAllCustomizedProducts() {
        logger.info("Starting indexing of all customized products.");
        var customizedProducts = customizedProductJpaRepository.findAll();
        logger.info("Found {} customized products to index.", customizedProducts.size());
        if (!customizedProducts.isEmpty()) {
            customizedProductElasticsearchRepository.saveAll(customizedProducts);
            logger.info("Successfully indexed {} customized products.", customizedProducts.size());
        } else {
            logger.warn("No customized products found to index.");
        }
    }

    @Async
    public void indexAllCustomizedOptions() {
        logger.info("Starting indexing of all customized options.");
        var customizedOptions = customizedOptionJpaRepository.findAll();
        logger.info("Found {} customized options to index.", customizedOptions.size());
        if (!customizedOptions.isEmpty()) {
            customizedOptionElasticsearchRepository.saveAll(customizedOptions);
            logger.info("Successfully indexed {} customized options.", customizedOptions.size());
        } else {
            logger.warn("No customized options found to index.");
        }
    }

    @Async
    public void indexAllCustomizedMeasurements() {
        logger.info("Starting indexing of all customized measurements.");
        var customizedMeasurements = customizedMeasurementJpaRepository.findAll();
        logger.info("Found {} customized measurements to index.", customizedMeasurements.size());
        if (!customizedMeasurements.isEmpty()) {
            customizedMeasurementElasticsearchRepository.saveAll(customizedMeasurements);
            logger.info("Successfully indexed {} customized measurements.", customizedMeasurements.size());
        } else {
            logger.warn("No customized measurements found to index.");
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application is ready. Starting customized product, option, and measurement indexing.");
        indexAllCustomizedProducts();
        indexAllCustomizedOptions();
        indexAllCustomizedMeasurements();
    }
}
