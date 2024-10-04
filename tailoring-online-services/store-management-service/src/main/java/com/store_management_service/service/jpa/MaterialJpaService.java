package com.store_management_service.service.jpa;

import com.store_management_service.dto.MaterialDto;
import com.store_management_service.exception.MaterialNotFoundException;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.mapper.MaterialMapper;
import com.store_management_service.model.Material;
import com.store_management_service.repository.jpa.MaterialJpaRepository;
import com.store_management_service.repository.jpa.StoreJpaRepository;
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
public class MaterialJpaService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialJpaService.class);
    private final MaterialJpaRepository materialRepository;
    private final MaterialMapper materialMapper;
    private final StoreJpaRepository storeRepository;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<MaterialDto> getAllMaterials(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all materials with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Material> materialPage = materialRepository.findAll(pageable);
        logger.info("Fetched {} materials", materialPage.getTotalElements());

        List<MaterialDto> materialDtos = materialPage.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(materialDtos, pageable, materialPage.getTotalElements());
    }

    public Page<Material> getAllMaterialsByStore(Long id, int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching materials for store with ID: {} with page: {}, size: {}, sortField: {}, sortDirection: {}", id, page, size, sortField, sortDirection);
        var store = storeRepository.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
        Pageable pageable = PageRequest.of(page, size);
        Page<Material> materialsPage = materialRepository.getAllByStoreId(store.getId(), pageable);
        logger.info("Fetched {} materials for store ID: {}", materialsPage.getTotalElements(), id);
        return materialsPage;
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
