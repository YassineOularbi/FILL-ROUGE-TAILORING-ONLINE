package com.order_management_service.service.jpa;

import com.order_management_service.client.StoreManagementClient;
import com.order_management_service.dto.CustomizedProductDto;
import com.order_management_service.exception.CustomizedProductNotFoundException;
import com.order_management_service.exception.ProductNotFoundException;
import com.order_management_service.mapper.CustomizedProductMapper;
import com.order_management_service.model.CustomizedProduct;
import com.order_management_service.repository.jpa.CustomizedProductJpaRepository;
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
public class CustomizedProductJpaService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedProductJpaService.class);
    private final CustomizedProductJpaRepository customizedProductRepository;
    private final CustomizedProductMapper customizedProductMapper;
    private final StoreManagementClient storeManagementClient;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<CustomizedProduct> getAllCustomizedProducts(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all customized products with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CustomizedProduct> customizedProducts = customizedProductRepository.findAll(pageable);

        logger.info("Fetched {} customized products", customizedProducts.getTotalElements());
        return customizedProducts;
    }

    public CustomizedProduct getCustomizedProductById(Long id) {
        logger.info("Fetching customized product with ID: {}", id);
        return customizedProductRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized product not found with ID: {}", id);
            return new CustomizedProductNotFoundException(id);
        });
    }

    public CustomizedProductDto getCustomizedProductWithProduct(Long id) {
        logger.info("Fetching customized product with product details for ID: {}", id);
        var customizedProduct = customizedProductRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized product not found with ID: {}", id);
            return new CustomizedProductNotFoundException(id);
        });
        var product = storeManagementClient.getProductById(String.valueOf(customizedProduct.getProductId())).orElseThrow(() -> {
            logger.error("Product not found with ID: {}", customizedProduct.getProductId());
            return new ProductNotFoundException(customizedProduct.getProductId());
        });
        var mappedCustomizedProduct = customizedProductMapper.toDto(customizedProduct);
        mappedCustomizedProduct.setProduct(product);
        logger.info("Fetched customized product with product details successfully for ID: {}", id);
        return mappedCustomizedProduct;
    }

    public CustomizedProductDto addCustomizedProduct(CustomizedProductDto customizedProductDto, Long productId) {
        logger.info("Adding customized product for product ID: {}", productId);
        var product = storeManagementClient.getProductById(String.valueOf(productId)).orElseThrow(() -> {
            logger.error("Product not found with ID: {}", productId);
            return new ProductNotFoundException(productId);
        });
        var mappedCustomizedProduct = customizedProductMapper.toEntity(customizedProductDto);
        mappedCustomizedProduct.setProductId(product.getId());
        var savedCustomizedProduct = customizedProductRepository.save(mappedCustomizedProduct);
        logger.info("Customized product added successfully for product ID: {}", productId);
        return customizedProductMapper.toDto(savedCustomizedProduct);
    }

    public CustomizedProductDto updateCustomizedProduct(Long id, CustomizedProductDto customizedProductDto) {
        logger.info("Updating customized product with ID: {}", id);
        var existingCustomizedProduct = customizedProductRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized product not found with ID: {}", id);
            return new CustomizedProductNotFoundException(id);
        });
        var updatedCustomizedProduct = customizedProductMapper.partialUpdate(customizedProductDto, existingCustomizedProduct);
        var savedCustomizedProduct = customizedProductRepository.save(updatedCustomizedProduct);
        logger.info("Customized product updated successfully with ID: {}", id);
        return customizedProductMapper.toDto(savedCustomizedProduct);
    }

    public void deleteCustomizedProduct(Long id) {
        logger.info("Deleting customized product with ID: {}", id);
        var customizedProduct = customizedProductRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized product not found with ID: {}", id);
            return new CustomizedProductNotFoundException(id);
        });
        customizedProductRepository.delete(customizedProduct);
        logger.info("Customized product deleted successfully with ID: {}", id);
    }
}
