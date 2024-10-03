package com.store_management_service.service.jpa;

import com.store_management_service.dto.MeasurementDto;
import com.store_management_service.exception.MeasurementNotFoundException;
import com.store_management_service.mapper.MeasurementMapper;
import com.store_management_service.model.Measurement;
import com.store_management_service.repository.jpa.MeasurementJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MeasurementJpaService {

    private static final Logger logger = LoggerFactory.getLogger(MeasurementJpaService.class);
    private final MeasurementJpaRepository measurementRepository;
    private final MeasurementMapper measurementMapper;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<MeasurementDto> getAllMeasurements(int page, int size) {
        logger.info("Fetching all measurements with page: {}, size: {}", page, size);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Measurement> measurementPage = measurementRepository.findAll(pageable);
        logger.info("Fetched {} measurements", measurementPage.getTotalElements());

        List<MeasurementDto> measurementDtos = measurementPage.stream()
                .map(measurementMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(measurementDtos, pageable, measurementPage.getTotalElements());
    }

    public Measurement getMeasurementById(Long id) {
        logger.info("Fetching measurement with ID: {}", id);
        return measurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Measurement not found with ID: {}", id);
            return new MeasurementNotFoundException(id);
        });
    }

    public MeasurementDto addMeasurement(MeasurementDto measurementDto) {
        logger.info("Adding new measurement");
        Measurement measurement = measurementMapper.toEntity(measurementDto);
        Measurement savedMeasurement = measurementRepository.save(measurement);
        return measurementMapper.toDto(savedMeasurement);
    }

    public MeasurementDto updateMeasurement(Long id, MeasurementDto measurementDto) {
        logger.info("Updating measurement with ID: {}", id);
        Measurement existingMeasurement = measurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Measurement not found with ID: {}", id);
            return new MeasurementNotFoundException(id);
        });
        Measurement updatedMeasurement = measurementMapper.partialUpdate(measurementDto, existingMeasurement);
        Measurement savedMeasurement = measurementRepository.save(updatedMeasurement);
        return measurementMapper.toDto(savedMeasurement);
    }

    public void deleteMeasurement(Long id) {
        logger.info("Deleting measurement with ID: {}", id);
        Measurement measurement = measurementRepository.findById(id).orElseThrow(() -> {
            logger.error("Measurement not found with ID: {}", id);
            return new MeasurementNotFoundException(id);
        });
        measurementRepository.delete(measurement);
    }
}
