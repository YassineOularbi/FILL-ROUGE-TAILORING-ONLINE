package com.order_management_service.service;

import com.order_management_service.client.StoreManagementClient;
import com.order_management_service.dto.CustomizedOptionDto;
import com.order_management_service.exception.CustomizedOptionNotFoundException;
import com.order_management_service.exception.CustomizedProductNotFoundException;
import com.order_management_service.exception.MaterialNotFoundException;
import com.order_management_service.mapper.CustomizedOptionMapper;
import com.order_management_service.model.CustomizedOption;
import com.order_management_service.repository.CustomizedOptionRepository;
import com.order_management_service.repository.CustomizedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomizedOptionService {

    private final CustomizedOptionRepository customizedOptionRepository;
    private final CustomizedOptionMapper customizedOptionMapper;
    private final StoreManagementClient storeManagementClient;
    private final CustomizedProductRepository customizedProductRepository;

    public List<CustomizedOption> getAllCustomizedOptions() {
        return customizedOptionRepository.findAll();
    }

    public CustomizedOption getCustomizedOptionById(Long id) {
        return customizedOptionRepository.findById(id).orElseThrow(() -> new CustomizedOptionNotFoundException(id));
    }

    public CustomizedOptionDto getCustomizedOptionWithDetails(Long id) {
        var customizedOption = customizedOptionRepository.findById(id).orElseThrow(() -> new CustomizedOptionNotFoundException(id));
        var material = storeManagementClient.getMaterialById(String.valueOf(customizedOption.getMaterialId())).orElseThrow(() -> new MaterialNotFoundException(customizedOption.getMaterialId()));
        var mappedOption = customizedOptionMapper.toDto(customizedOption);
        mappedOption.setMaterial(material);
        return mappedOption;
    }

    public CustomizedOptionDto addCustomizedOption(CustomizedOptionDto customizedOptionDto, Long customizedProductId, Long materialId) {
        var customizedProduct = customizedProductRepository.findById(customizedProductId).orElseThrow(() -> new CustomizedProductNotFoundException(customizedProductId));
        var material = storeManagementClient.getMaterialById(String.valueOf(materialId)).orElseThrow(() -> new MaterialNotFoundException(materialId));
        var existingOption = customizedOptionRepository.findByMaterialIdAndProductId(material.getId(), customizedProduct.getId());
        if (existingOption.isPresent()) {
            return updateCustomizedOption(existingOption.get().getId(), customizedOptionDto);
        } else {
            var mappedOption = customizedOptionMapper.toEntity(customizedOptionDto);
            mappedOption.setMaterialId(material.getId());
            mappedOption.setProduct(customizedProduct);
            var savedOption = customizedOptionRepository.save(mappedOption);
            return customizedOptionMapper.toDto(savedOption);
        }
    }

    public CustomizedOptionDto updateCustomizedOption(Long id, CustomizedOptionDto customizedOptionDto) {
        var existingCustomizedOption = customizedOptionRepository.findById(id).orElseThrow(() -> new CustomizedOptionNotFoundException(id));
        var updatedOption = customizedOptionMapper.partialUpdate(customizedOptionDto, existingCustomizedOption);
        var savedOption = customizedOptionRepository.save(updatedOption);
        return customizedOptionMapper.toDto(savedOption);
    }

    public void deleteCustomizedOption(Long id) {
        var customizedOption = customizedOptionRepository.findById(id).orElseThrow(() -> new CustomizedOptionNotFoundException(id));
        customizedOptionRepository.delete(customizedOption);
    }
}
