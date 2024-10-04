package com.order_management_service.service.jpa;

import com.order_management_service.client.StoreManagementClient;
import com.order_management_service.dto.CustomizedOptionDto;
import com.order_management_service.exception.CustomizedOptionNotFoundException;
import com.order_management_service.exception.CustomizedProductNotFoundException;
import com.order_management_service.exception.MaterialNotFoundException;
import com.order_management_service.mapper.CustomizedOptionMapper;
import com.order_management_service.model.CustomizedOption;
import com.order_management_service.repository.jpa.CustomizedOptionJpaRepository;
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
public class CustomizedOptionJpaService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedOptionJpaService.class);
    private final CustomizedOptionJpaRepository customizedOptionRepository;
    private final CustomizedOptionMapper customizedOptionMapper;
    private final StoreManagementClient storeManagementClient;
    private final CustomizedProductJpaRepository customizedProductRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<CustomizedOption> getAllCustomizedOptions(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all customized options with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CustomizedOption> customizedOptions = customizedOptionRepository.findAll(pageable);

        logger.info("Fetched {} customized options", customizedOptions.getTotalElements());
        return customizedOptions;
    }

    public CustomizedOption getCustomizedOptionById(Long id) {
        logger.info("Fetching customized option with ID: {}", id);
        return customizedOptionRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized option not found with ID: {}", id);
            return new CustomizedOptionNotFoundException(id);
        });
    }

    public CustomizedOptionDto getCustomizedOptionWithDetails(Long id) {
        logger.info("Fetching customized option with details for ID: {}", id);
        var customizedOption = customizedOptionRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized option not found with ID: {}", id);
            return new CustomizedOptionNotFoundException(id);
        });
        var material = storeManagementClient.getMaterialById(String.valueOf(customizedOption.getMaterialId())).orElseThrow(() -> {
            logger.error("Material not found with ID: {}", customizedOption.getMaterialId());
            return new MaterialNotFoundException(customizedOption.getMaterialId());
        });
        var mappedOption = customizedOptionMapper.toDto(customizedOption);
        mappedOption.setMaterial(material);
        logger.info("Fetched customized option with details successfully for ID: {}", id);
        return mappedOption;
    }

    public CustomizedOptionDto addCustomizedOption(CustomizedOptionDto customizedOptionDto, Long customizedProductId, Long materialId) {
        logger.info("Adding customized option for product ID: {} and material ID: {}", customizedProductId, materialId);
        var customizedProduct = customizedProductRepository.findById(customizedProductId).orElseThrow(() -> {
            logger.error("Customized product not found with ID: {}", customizedProductId);
            return new CustomizedProductNotFoundException(customizedProductId);
        });
        var material = storeManagementClient.getMaterialById(String.valueOf(materialId)).orElseThrow(() -> {
            logger.error("Material not found with ID: {}", materialId);
            return new MaterialNotFoundException(materialId);
        });
        var existingOption = customizedOptionRepository.findByMaterialIdAndProductId(material.getId(), customizedProduct.getId());
        if (existingOption.isPresent()) {
            logger.info("Customized option already exists, updating it for ID: {}", existingOption.get().getId());
            return updateCustomizedOption(existingOption.get().getId(), customizedOptionDto);
        } else {
            var mappedOption = customizedOptionMapper.toEntity(customizedOptionDto);
            mappedOption.setMaterialId(material.getId());
            mappedOption.setProduct(customizedProduct);
            var savedOption = customizedOptionRepository.save(mappedOption);
            logger.info("Customized option added successfully for product ID: {} and material ID: {}", customizedProductId, materialId);
            return customizedOptionMapper.toDto(savedOption);
        }
    }

    public CustomizedOptionDto updateCustomizedOption(Long id, CustomizedOptionDto customizedOptionDto) {
        logger.info("Updating customized option with ID: {}", id);
        var existingCustomizedOption = customizedOptionRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized option not found with ID: {}", id);
            return new CustomizedOptionNotFoundException(id);
        });
        var updatedOption = customizedOptionMapper.partialUpdate(customizedOptionDto, existingCustomizedOption);
        var savedOption = customizedOptionRepository.save(updatedOption);
        logger.info("Customized option updated successfully with ID: {}", id);
        return customizedOptionMapper.toDto(savedOption);
    }

    public void deleteCustomizedOption(Long id) {
        logger.info("Deleting customized option with ID: {}", id);
        var customizedOption = customizedOptionRepository.findById(id).orElseThrow(() -> {
            logger.error("Customized option not found with ID: {}", id);
            return new CustomizedOptionNotFoundException(id);
        });
        customizedOptionRepository.delete(customizedOption);
        logger.info("Customized option deleted successfully with ID: {}", id);
    }
}
