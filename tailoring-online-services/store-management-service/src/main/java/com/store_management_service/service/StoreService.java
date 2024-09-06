package com.store_management_service.service;

import com.store_management_service.client.UserManagementClient;
import com.store_management_service.dto.StoreDto;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.exception.TailorNotFoundException;
import com.store_management_service.mapper.StoreMapper;
import com.store_management_service.model.Store;
import com.store_management_service.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final UserManagementClient userManagementClient;

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
    }

    public StoreDto getStoreWithTailor(Long id) {
        var store = storeRepository.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
        var tailor = userManagementClient.getTailorById(store.getTailorId()).orElseThrow(() -> new TailorNotFoundException(store.getTailorId()));
        var mappedStore = storeMapper.toDto(store);
        mappedStore.setTailor(tailor);
        return mappedStore;
    }

    public StoreDto addStore(StoreDto storeDto, String tailorId) {
        var tailor = userManagementClient.getTailorById(tailorId).orElseThrow(() -> new  TailorNotFoundException(tailorId));
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
        var existingStore = storeRepository.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
        var updatedStore = storeMapper.partialUpdate(storeDto, existingStore);
        var savedStore = storeRepository.save(updatedStore);
        return storeMapper.toDto(savedStore);
    }

    public void deleteStore(Long id) {
        var store = storeRepository.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
        storeRepository.delete(store);
    }
}
