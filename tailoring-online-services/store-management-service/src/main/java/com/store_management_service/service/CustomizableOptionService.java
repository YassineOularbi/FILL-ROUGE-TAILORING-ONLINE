package com.store_management_service.service;

import com.store_management_service.dto.CustomizableOptionDto;
import com.store_management_service.enums.MaterialType;
import com.store_management_service.exception.CustomizableOptionExistException;
import com.store_management_service.exception.CustomizableOptionNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.mapper.CustomizableOptionMapper;
import com.store_management_service.model.CustomizableOption;
import com.store_management_service.repository.CustomizableOptionRepository;
import com.store_management_service.repository.ThreeDModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomizableOptionService {

    private final CustomizableOptionRepository customizableOptionRepository;
    private final CustomizableOptionMapper customizableOptionMapper;
    private final ThreeDModelRepository threeDModelRepository;

    public List<CustomizableOptionDto> getAllCustomizableOptions(){
        return customizableOptionMapper.toDtos(customizableOptionRepository.findAll());
    }

    public List<CustomizableOption> getAllCustomizableOptionsByThreeDModel(Long id){
        var threeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));
        return customizableOptionRepository.getAllByModel(threeDModel);
    }

    public CustomizableOption getCustomizableOptionById(Long id) {
        return customizableOptionRepository.findById(id).orElseThrow(() -> new CustomizableOptionNotFoundException(id));
    }

    public CustomizableOptionDto addCustomizableOption(Long id, MaterialType type) {
        var threeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));
        if (customizableOptionRepository.existsByModelAndMaterialType(threeDModel, type)){
            throw new CustomizableOptionExistException(threeDModel.getId(), type);
        }
        var customizableOption = new CustomizableOption();
        customizableOption.setModel(threeDModel);
        customizableOption.setType(type);
        var savedCustomizableOption = customizableOptionRepository.save(customizableOption);
        return customizableOptionMapper.toDto(savedCustomizableOption);
    }

    public void deleteCustomizableOption(Long id) {
        var existingCustomizableOption = customizableOptionRepository.findById(id).orElseThrow(() -> new CustomizableOptionNotFoundException(id));
        customizableOptionRepository.delete(existingCustomizableOption);
    }

}
