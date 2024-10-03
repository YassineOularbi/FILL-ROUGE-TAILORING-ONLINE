package com.store_management_service.service.jpa;

import com.store_management_service.dto.MaterialOptionDto;
import com.store_management_service.exception.*;
import com.store_management_service.mapper.MaterialOptionMapper;
import com.store_management_service.model.MaterialOption;
import com.store_management_service.model.MaterialOptionKey;
import com.store_management_service.repository.jpa.CustomizableOptionJpaRepository;
import com.store_management_service.repository.jpa.MaterialOptionJpaRepository;
import com.store_management_service.repository.jpa.MaterialJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialOptionJpaService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialOptionJpaService.class);
    private final MaterialOptionJpaRepository materialOptionRepository;
    private final MaterialOptionMapper materialOptionMapper;
    private final CustomizableOptionJpaRepository customizableOptionRepository;
    private final MaterialJpaRepository materialRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<MaterialOptionDto> getAllMaterialOptions(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all material options with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<MaterialOption> materialOptionsPage = materialOptionRepository.findAll(pageable);
        logger.info("Fetched {} material options", materialOptionsPage.getTotalElements());

        List<MaterialOptionDto> materialOptionDtos = materialOptionsPage.stream()
                .map(materialOptionMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(materialOptionDtos, pageable, materialOptionsPage.getTotalElements());
    }

    public List<MaterialOptionDto> getAllMaterialOptions() {
        var materialOptions = materialOptionRepository.findAll();
        return materialOptionMapper.toDtos(materialOptions);
    }

    public List<MaterialOption> getAllMaterialOptionsByCustomizableOption(Long id) {
        var option = customizableOptionRepository.findById(id).orElseThrow(() -> new CustomizableOptionNotFoundException(id));
        return materialOptionRepository.getAllByOption(option);
    }

    public MaterialOption getMaterialOptionById(Long materialId, Long optionId) {
        var material = materialRepository.findById(materialId).orElseThrow(() -> new MaterialNotFoundException(materialId));
        var option = customizableOptionRepository.findById(optionId).orElseThrow(() -> new CustomizableOptionNotFoundException(optionId));
        var id = new MaterialOptionKey(material.getId(), option.getId());
        return materialOptionRepository.findById(id).orElseThrow(() -> new MaterialOptionNotFoundException(id));
    }

    public MaterialOptionDto addMaterialOption(Long materialId, Long optionId) {
        var material = materialRepository.findById(materialId).orElseThrow(() -> new MaterialNotFoundException(materialId));
        var option = customizableOptionRepository.findById(optionId).orElseThrow(() -> new CustomizableOptionNotFoundException(optionId));
        var id = new MaterialOptionKey(material.getId(), option.getId());
        if (materialOptionRepository.existsById(id)) {
            throw new MaterialOptionExistException(id);
        }
        if (material.getType() != option.getType()) {
            throw new CustomizableOptionMaterialException(material, option);
        }
        var materialOption = new MaterialOption(id, material, option);
        var savedMaterialOption = materialOptionRepository.save(materialOption);
        return materialOptionMapper.toDto(savedMaterialOption);
    }

    public void deleteMaterialOption(Long materialId, Long optionId) {
        var material = materialRepository.findById(materialId).orElseThrow(() -> new MaterialNotFoundException(materialId));
        var option = customizableOptionRepository.findById(optionId).orElseThrow(() -> new CustomizableOptionNotFoundException(optionId));
        var id = new MaterialOptionKey(material.getId(), option.getId());
        var existingMaterialOption = materialOptionRepository.findById(id).orElseThrow(() -> new MaterialOptionNotFoundException(id));
        materialOptionRepository.delete(existingMaterialOption);
    }
}
