package com.store_management_service.service;

import com.store_management_service.dto.MaterialDto;
import com.store_management_service.exception.MaterialNotFoundException;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.mapper.MaterialMapper;
import com.store_management_service.model.Material;
import com.store_management_service.repository.MaterialRepository;
import com.store_management_service.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final StoreRepository storeRepository;

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public List<Material> getAllMaterialsByStore(Long id){
        return materialRepository.getAllByStoreId(id);
    }

    public Material getMaterialById(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new MaterialNotFoundException(id));
    }

    public MaterialDto addMaterial(MaterialDto materialDto, Long storeId) {
        var store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException(storeId));
        Material material = materialMapper.toEntity(materialDto);
        material.setStore(store);
        Material savedMaterial = materialRepository.save(material);
        return materialMapper.toDto(savedMaterial);
    }

    public MaterialDto updateMaterial(Long id, MaterialDto materialDto) {
        Material existingMaterial = materialRepository.findById(id).orElseThrow(() -> new MaterialNotFoundException(id));
        Material updatedMaterial = materialMapper.partialUpdate(materialDto, existingMaterial);
        Material savedMaterial = materialRepository.save(updatedMaterial);
        return materialMapper.toDto(savedMaterial);
    }

    public void deleteMaterial(Long id) {
        Material material = materialRepository.findById(id).orElseThrow(() -> new MaterialNotFoundException(id));
        materialRepository.delete(material);
    }
}
