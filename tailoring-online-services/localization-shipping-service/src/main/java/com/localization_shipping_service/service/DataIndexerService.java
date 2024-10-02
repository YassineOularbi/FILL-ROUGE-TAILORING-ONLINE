package com.localization_shipping_service.service;

import com.localization_shipping_service.model.Address;
import com.localization_shipping_service.repository.elasticsearch.AddressElasticsearchRepository;
import com.localization_shipping_service.repository.jpa.AddressJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataIndexerService {
    private final AddressJpaRepository addressJpaRepository;

    private final AddressElasticsearchRepository addressElasticsearchRepository;

    @Async
    public void indexAllAddresses() {
        List<Address> addresses = addressJpaRepository.findAll();
        addressElasticsearchRepository.saveAll(addresses);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        indexAllAddresses();
    }

//    @PostPersist
//    @PostUpdate
//    public void onPostPersistOrUpdate(Address address) {
//        addressElasticsearchRepository.save(address);
//    }
//
//    @PostRemove
//    public void onPostRemove(Address address) {
//        addressElasticsearchRepository.delete(address);
//    }
}
