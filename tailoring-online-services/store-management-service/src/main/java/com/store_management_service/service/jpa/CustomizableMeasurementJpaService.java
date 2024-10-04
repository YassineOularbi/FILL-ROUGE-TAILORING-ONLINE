package com.store_management_service.service.jpa;

import com.store_management_service.dto.CustomizableMeasurementDto;
import com.store_management_service.exception.CustomizableMeasurementExistException;
import com.store_management_service.exception.CustomizableMeasurementNotFoundException;
import com.store_management_service.exception.MeasurementNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.mapper.CustomizableMeasurementMapper;
import com.store_management_service.model.CustomizableMeasurement;
import com.store_management_service.model.CustomizableMeasurementKey;
import com.store_management_service.repository.jpa.CustomizableMeasurementJpaRepository;
import com.store_management_service.repository.jpa.MeasurementJpaRepository;
import com.store_management_service.repository.jpa.ThreeDModelJpaRepository;
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
public class CustomizableMeasurementJpaService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizableMeasurementJpaService.class);
    private final CustomizableMeasurementJpaRepository customizableMeasurementRepository;
    private final CustomizableMeasurementMapper customizableMeasurementMapper;
    private final ThreeDModelJpaRepository threeDModelRepository;
    private final MeasurementJpaRepository measurementRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<CustomizableMeasurementDto> getAllCustomizableMeasurement(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all customizable measurements with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CustomizableMeasurement> measurements = customizableMeasurementRepository.findAll(pageable);

        logger.info("Fetched {} customizable measurements", measurements.getTotalElements());
        return measurements.map(customizableMeasurementMapper::toDto);
    }

    public Page<CustomizableMeasurement> getAllCustomizableMeasurementByThreeDModel(Long id, int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching customizable measurements for ThreeDModel ID: {}", id);
        var threeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CustomizableMeasurement> measurements = customizableMeasurementRepository.findAllByModel(threeDModel, pageable);

        logger.info("Fetched {} customizable measurements for ThreeDModel ID: {}", measurements.getTotalElements(), id);
        return measurements;
    }

    public CustomizableMeasurement getCustomizableMeasurementById(Long threeDModelId, Long measurementId) {
        logger.info("Fetching customizable measurement with ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);
        var threeDModel = threeDModelRepository.findById(threeDModelId).orElseThrow(() -> new ThreeDModelNotFoundException(threeDModelId));
        var measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var id = new CustomizableMeasurementKey(threeDModel.getId(), measurement.getId());

        return customizableMeasurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Customizable measurement not found with ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);
            return new CustomizableMeasurementNotFoundException(id);
        });
    }

    public CustomizableMeasurementDto addCustomizableMeasurement(Long threeDModelId, Long measurementId) {
        logger.info("Adding customizable measurement for ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);
        var threeDModel = threeDModelRepository.findById(threeDModelId).orElseThrow(() -> new ThreeDModelNotFoundException(threeDModelId));
        var measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var id = new CustomizableMeasurementKey(threeDModel.getId(), measurement.getId());

        if (customizableMeasurementRepository.existsById(id)) {
            logger.error("Customizable measurement already exists with ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);
            throw new CustomizableMeasurementExistException(id);
        }

        var customizableMeasurement = new CustomizableMeasurement(id, threeDModel, measurement);
        var savedCustomizableMeasurement = customizableMeasurementRepository.save(customizableMeasurement);
        logger.info("Customizable measurement added successfully for ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);

        return customizableMeasurementMapper.toDto(savedCustomizableMeasurement);
    }

    public void deleteCustomizableMeasurement(Long threeDModelId, Long measurementId) {
        logger.info("Deleting customizable measurement with ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);
        var threeDModel = threeDModelRepository.findById(threeDModelId).orElseThrow(() -> new ThreeDModelNotFoundException(threeDModelId));
        var measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var id = new CustomizableMeasurementKey(threeDModel.getId(), measurement.getId());

        var existingCustomizableMeasurement = customizableMeasurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Customizable measurement not found with ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);
            return new CustomizableMeasurementNotFoundException(id);
        });

        customizableMeasurementRepository.delete(existingCustomizableMeasurement);
        logger.info("Customizable measurement deleted successfully with ThreeDModel ID: {} and Measurement ID: {}", threeDModelId, measurementId);
    }
}
