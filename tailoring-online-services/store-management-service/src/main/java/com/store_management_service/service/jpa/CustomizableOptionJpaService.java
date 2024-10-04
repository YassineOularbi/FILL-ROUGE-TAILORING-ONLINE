package com.store_management_service.service.jpa;

import com.store_management_service.dto.CustomizableOptionDto;
import com.store_management_service.enums.MaterialType;
import com.store_management_service.exception.CustomizableOptionExistException;
import com.store_management_service.exception.CustomizableOptionNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.mapper.CustomizableOptionMapper;
import com.store_management_service.model.CustomizableOption;
import com.store_management_service.repository.jpa.CustomizableOptionJpaRepository;
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

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomizableOptionJpaService {

    private static final Logger logger = LoggerFactory.getLogger(CustomizableOptionJpaService.class);
    private final CustomizableOptionJpaRepository customizableOptionRepository;
    private final CustomizableOptionMapper customizableOptionMapper;
    private final ThreeDModelJpaRepository threeDModelRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<CustomizableOptionDto> getAllCustomizableOptions(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all customizable options with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<CustomizableOption> optionsPage = customizableOptionRepository.findAll(pageable);

        logger.info("Fetched {} customizable options", optionsPage.getTotalElements());
        return optionsPage.map(customizableOptionMapper::toDto);
    }

    public List<CustomizableOption> getAllCustomizableOptionsByThreeDModel(Long id) {
        var threeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));
        return customizableOptionRepository.getAllByModel(threeDModel);
    }

    public CustomizableOption getCustomizableOptionById(Long id) {
        logger.info("Fetching customizable option with ID: {}", id);
        return customizableOptionRepository.findById(id).orElseThrow(() -> {
            logger.error("Customizable option not found with ID: {}", id);
            return new CustomizableOptionNotFoundException(id);
        });
    }

    public CustomizableOptionDto addCustomizableOption(Long id, MaterialType type) {
        logger.info("Adding customizable option for 3D Model ID: {} with Material Type: {}", id, type);
        var threeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));
        if (customizableOptionRepository.existsByModelAndType(threeDModel, type)){
            throw new CustomizableOptionExistException(threeDModel.getId(), type);
        }
        var customizableOption = new CustomizableOption();
        customizableOption.setModel(threeDModel);
        customizableOption.setType(type);
        var savedCustomizableOption = customizableOptionRepository.save(customizableOption);
        logger.info("Customizable option added successfully for 3D Model ID: {}", id);
        return customizableOptionMapper.toDto(savedCustomizableOption);
    }

    public void deleteCustomizableOption(Long id) {
        logger.info("Deleting customizable option with ID: {}", id);
        var existingCustomizableOption = customizableOptionRepository.findById(id).orElseThrow(() -> {
            logger.error("Customizable option not found with ID: {}", id);
            return new CustomizableOptionNotFoundException(id);
        });
        customizableOptionRepository.delete(existingCustomizableOption);
        logger.info("Customizable option deleted successfully with ID: {}", id);
    }
}
