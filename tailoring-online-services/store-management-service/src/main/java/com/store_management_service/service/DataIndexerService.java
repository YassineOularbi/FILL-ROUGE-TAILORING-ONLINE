package com.store_management_service.service;

import com.store_management_service.model.Product;
import com.store_management_service.repository.elasticsearch.ProductElasticsearchRepository;
import com.store_management_service.repository.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DataIndexerService {

    private static final Logger logger = LoggerFactory.getLogger(DataIndexerService.class);

    private final ProductJpaRepository productJpaRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;

    @Async
    public void indexAllProducts() {
        logger.info("Starting indexing of all products.");

        var products = productJpaRepository.findAll();
        logger.info("Found {} products to index.", products.size());

        var simpleProducts = products.stream().map(product -> {
            var simpleProduct = new Product();
            simpleProduct.setId(product.getId());
            simpleProduct.setName(product.getName());
            simpleProduct.setDescription(product.getDescription());
            simpleProduct.setCategory(product.getCategory());
            simpleProduct.setPicture(product.getPicture());
            simpleProduct.setImages(product.getImages());
            simpleProduct.setDetails(product.getDetails());
            simpleProduct.setHistoricalStory(product.getHistoricalStory());
            simpleProduct.setCodeSKU(product.getCodeSKU());
            simpleProduct.setRating(product.getRating());
            simpleProduct.setSales(product.getSales());
            simpleProduct.setAuthenticityVerified(product.getAuthenticityVerified());

            return simpleProduct;
        }).collect(Collectors.toList());

        if (!simpleProducts.isEmpty()) {
            productElasticsearchRepository.saveAll(simpleProducts);
            logger.info("Successfully indexed {} products.", simpleProducts.size());
        } else {
            logger.warn("No products found to index.");
        }
    }


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        logger.info("Application is ready. Starting product indexing.");
        indexAllProducts();
    }
}
