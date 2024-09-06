package com.product_management_service.client;

import com.product_management_service.model.Store;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "store-management-service", url = "http://localhost:8091")
public interface StoreManagementClient {
    @GetMapping("/api/store/get-store-by-id/{id}")
    Optional<Store> getStoreById(@PathVariable("id") String id);
}
