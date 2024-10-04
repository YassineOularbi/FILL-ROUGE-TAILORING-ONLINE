package com.order_management_service.service.jpa;

import com.order_management_service.client.StoreManagementClient;
import com.order_management_service.dto.CustomizedMeasurementDto;
import com.order_management_service.exception.CustomizedMeasurementNotFoundException;
import com.order_management_service.exception.CustomizedProductNotFoundException;
import com.order_management_service.exception.MeasurementNotFoundException;
import com.order_management_service.mapper.CustomizedMeasurementMapper;
import com.order_management_service.model.CustomizedMeasurement;
import com.order_management_service.repository.jpa.CustomizedMeasurementJpaRepository;
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
public class CustomizedMeasurementJpaService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedMeasurementJpaService.class);
    private final CustomizedMeasurementJpaRepository customizedMeasurementRepository;
    private final CustomizedMeasurementMapper customizedMeasurementMapper;
    private final StoreManagementClient storeManagementClient;
    private final CustomizedProductJpaRepository customizedProductRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<CustomizedMeasurement> getAllCustomizedMeasurements(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all customized measurements with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CustomizedMeasurement> customizedMeasurements = customizedMeasurementRepository.findAll(pageable);

        logger.info("Fetched {} customized measurements", customizedMeasurements.getTotalElements());
        return customizedMeasurements;
    }

    public CustomizedMeasurement getCustomizedMeasurementById(Long id) {
        logger.info("Fetching customized measurement with ID: {}", id);
        return customizedMeasurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized measurement not found with ID: {}", id);
            return new CustomizedMeasurementNotFoundException(id);
        });
    }

    public CustomizedMeasurementDto getCustomizedMeasurementWithDetails(Long id) {
        logger.info("Fetching customized measurement with details for ID: {}", id);
        var customizedMeasurement = customizedMeasurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized measurement not found with ID: {}", id);
            return new CustomizedMeasurementNotFoundException(id);
        });
        var measurement = storeManagementClient.getMeasurementById(String.valueOf(customizedMeasurement.getMeasurementId())).orElseThrow(() -> {
            logger.error("Measurement not found with ID: {}", customizedMeasurement.getMeasurementId());
            return new MeasurementNotFoundException(customizedMeasurement.getMeasurementId());
        });
        var mappedMeasurement = customizedMeasurementMapper.toDto(customizedMeasurement);
        mappedMeasurement.setMeasurement(measurement);
        logger.info("Fetched customized measurement with details successfully for ID: {}", id);
        return mappedMeasurement;
    }

    public CustomizedMeasurementDto addCustomizedMeasurement(CustomizedMeasurementDto customizedMeasurementDto, Long customizedProductId, Long measurementId) {
        logger.info("Adding customized measurement for product ID: {} and measurement ID: {}", customizedProductId, measurementId);
        var customizedProduct = customizedProductRepository.findById(customizedProductId).orElseThrow(() -> {
            logger.error("Customized product not found with ID: {}", customizedProductId);
            return new CustomizedProductNotFoundException(customizedProductId);
        });
        var measurement = storeManagementClient.getMeasurementById(String.valueOf(measurementId)).orElseThrow(() -> {
            logger.error("Measurement not found with ID: {}", measurementId);
            return new MeasurementNotFoundException(measurementId);
        });
        var existingMeasurement = customizedMeasurementRepository.findByMeasurementIdAndProductId(measurement.getId(), customizedProduct.getId());
        if (existingMeasurement.isPresent()) {
            logger.info("Customized measurement already exists, updating it for ID: {}", existingMeasurement.get().getId());
            return updateCustomizedMeasurement(existingMeasurement.get().getId(), customizedMeasurementDto);
        } else {
            var mappedMeasurement = customizedMeasurementMapper.toEntity(customizedMeasurementDto);
            mappedMeasurement.setMeasurementId(measurement.getId());
            mappedMeasurement.setProduct(customizedProduct);
            var savedMeasurement = customizedMeasurementRepository.save(mappedMeasurement);
            logger.info("Customized measurement added successfully for product ID: {} and measurement ID: {}", customizedProductId, measurementId);
            return customizedMeasurementMapper.toDto(savedMeasurement);
        }
    }

    public CustomizedMeasurementDto updateCustomizedMeasurement(Long id, CustomizedMeasurementDto customizedMeasurementDto) {
        logger.info("Updating customized measurement with ID: {}", id);
        var existingCustomizedMeasurement = customizedMeasurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized measurement not found with ID: {}", id);
            return new CustomizedMeasurementNotFoundException(id);
        });
        var updatedMeasurement = customizedMeasurementMapper.partialUpdate(customizedMeasurementDto, existingCustomizedMeasurement);
        var savedMeasurement = customizedMeasurementRepository.save(updatedMeasurement);
        logger.info("Customized measurement updated successfully with ID: {}", id);
        return customizedMeasurementMapper.toDto(savedMeasurement);
    }

    public void deleteCustomizedMeasurement(Long id) {
        logger.info("Deleting customized measurement with ID: {}", id);
        var customizedMeasurement = customizedMeasurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized measurement not found with ID: {}", id);
            return new CustomizedMeasurementNotFoundException(id);
        });
        customizedMeasurementRepository.delete(customizedMeasurement);
        logger.info("Customized measurement deleted successfully with ID: {}", id);
    }
}
