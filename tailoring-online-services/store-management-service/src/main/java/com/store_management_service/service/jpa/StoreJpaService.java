package com.store_management_service.service.jpa;

import com.store_management_service.client.UserManagementClient;
import com.store_management_service.dto.StoreDto;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.exception.TailorNotFoundException;
import com.store_management_service.mapper.StoreMapper;
import com.store_management_service.model.Store;
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
public class StoreJpaService {

    private static final Logger logger = LoggerFactory.getLogger(StoreJpaService.class);
    private final StoreJpaRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserManagementClient userManagementClient;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<StoreDto> getAllStores(int page, int size) {
        logger.info("Fetching all stores with page: {}, size: {}", page, size);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Store> storePage = storeRepository.findAll(pageable);
        logger.info("Fetched {} stores", storePage.getTotalElements());

        List<StoreDto> storeDtos = storePage.stream()
                .map(storeMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(storeDtos, pageable, storePage.getTotalElements());
    }

    public Store getStoreById(Long id) {
        logger.info("Fetching store with ID: {}", id);
        return storeRepository.findById(id).orElseThrow(() -> {
            logger.error("Store not found with ID: {}", id);
            return new StoreNotFoundException(id);
        });
    }

    public StoreDto getStoreWithTailor(Long id) {
        var store = storeRepository.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
        var tailor = userManagementClient.getTailorById(store.getTailorId()).orElseThrow(() -> new TailorNotFoundException(store.getTailorId()));
        var mappedStore = storeMapper.toDto(store);
        mappedStore.setTailor(tailor);
        return mappedStore;
    }

    public StoreDto addStore(StoreDto storeDto, String tailorId) {
        logger.info("Adding new store");
        var tailor = userManagementClient.getTailorById(tailorId).orElseThrow(() -> {
            logger.error("Tailor not found with ID: {}", tailorId);
            return new TailorNotFoundException(tailorId);
        });
        var store = storeRepository.findByTailorId(tailorId);
        if (store.isPresent()) {
            return updateStore(store.get().getId(), storeDto);
        } else {
            var mappedStore = storeMapper.toEntity(storeDto);
            mappedStore.setTailorId(tailor.getId());
            var savedStore = storeRepository.save(mappedStore);
            return storeMapper.toDto(savedStore);
        }
    }

    public StoreDto updateStore(Long id, StoreDto storeDto) {
        logger.info("Updating store with ID: {}", id);
        var existingStore = storeRepository.findById(id).orElseThrow(() -> {
            logger.error("Store not found with ID: {}", id);
            return new StoreNotFoundException(id);
        });
        var updatedStore = storeMapper.partialUpdate(storeDto, existingStore);
        var savedStore = storeRepository.save(updatedStore);
        return storeMapper.toDto(savedStore);
    }

    public void deleteStore(Long id) {
        logger.info("Deleting store with ID: {}", id);
        var store = storeRepository.findById(id).orElseThrow(() -> {
            logger.error("Store not found with ID: {}", id);
            return new StoreNotFoundException(id);
        });
        storeRepository.delete(store);
    }
}
