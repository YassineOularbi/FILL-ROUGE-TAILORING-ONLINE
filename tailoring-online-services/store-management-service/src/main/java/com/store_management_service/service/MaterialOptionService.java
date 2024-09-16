package com.store_management_service.service;

import com.store_management_service.dto.MaterialOptionDto;
import com.store_management_service.exception.*;
import com.store_management_service.mapper.MaterialOptionMapper;
import com.store_management_service.model.MaterialOption;
import com.store_management_service.model.MaterialOptionKey;
import com.store_management_service.repository.CustomizableOptionRepository;
import com.store_management_service.repository.MaterialOptionRepository;
import com.store_management_service.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialOptionService {

    private final MaterialOptionRepository materialOptionRepository;
    private final MaterialOptionMapper materialOptionMapper;
    private final CustomizableOptionRepository customizableOptionRepository;
    private final MaterialRepository materialRepository;

    public List<MaterialOptionDto> getAllMaterialOptions() {
        var materialOptions = materialOptionRepository.findAll();
        return materialOptionMapper.toDtos(materialOptions);
    }

    public List<MaterialOption> getAllMaterialOptionsByCustomizableOption(Long id){
        var option = customizableOptionRepository.findById(id).orElseThrow(() -> new CustomizableOptionNotFoundException(id));
        return materialOptionRepository.getAllByOption(option);
    }

    public MaterialOption getMaterialOptionById(Long materialId, Long optionId) {
        var material = materialRepository.findById(materialId).orElseThrow(() -> new MaterialNotFoundException(materialId));
        var option = customizableOptionRepository.findById(optionId).orElseThrow(() -> new  CustomizableOptionNotFoundException(optionId));
        var id = new MaterialOptionKey(material.getId(), option.getId());
        return materialOptionRepository.findById(id).orElseThrow(() -> new MaterialOptionNotFoundException(id));
    }

    public MaterialOptionDto addMaterialOption(Long materialId, Long optionId) {
        var material = materialRepository.findById(materialId).orElseThrow(() -> new MaterialNotFoundException(materialId));
        var option = customizableOptionRepository.findById(optionId).orElseThrow(() -> new  CustomizableOptionNotFoundException(optionId));
        var id = new MaterialOptionKey(material.getId(), option.getId());
        if (materialOptionRepository.existsById(id)){
            throw new MaterialOptionExistException(id);
        }
        if (!(material.getType() == option.getType())){
            throw new CustomizableOptionMaterialException(material, option);
        }
        var materialOption = new MaterialOption(id, material, option);
        var savedMaterialOption = materialOptionRepository.save(materialOption);
        return materialOptionMapper.toDto(savedMaterialOption);
    }

    public void deleteMaterialOption(Long materialId, Long optionId){
        var material = materialRepository.findById(materialId).orElseThrow(() -> new MaterialNotFoundException(materialId));
        var option = customizableOptionRepository.findById(optionId).orElseThrow(() -> new  CustomizableOptionNotFoundException(optionId));
        var id = new MaterialOptionKey(material.getId(), option.getId());
        var existingMaterialOption = materialOptionRepository.findById(id).orElseThrow(() -> new MaterialOptionNotFoundException(id));
        materialOptionRepository.delete(existingMaterialOption);
    }
}
