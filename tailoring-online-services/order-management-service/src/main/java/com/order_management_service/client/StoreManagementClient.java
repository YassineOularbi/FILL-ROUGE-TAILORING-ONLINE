package com.order_management_service.client;

import com.order_management_service.model.Material;
import com.order_management_service.model.Measurement;
import com.order_management_service.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "store-management-service", url = "http://localhost:8091")
public interface StoreManagementClient {

    @GetMapping("/api/product/get-product-by-id/{id}")
    Optional<Product> getProductById(@PathVariable("id") String id);

    @GetMapping("/api/measurement/get-measurement-by-id/{id}")
    Optional<Measurement> getMeasurementById(@PathVariable("id") String id);

    @GetMapping("/api/material/get-material-by-id/{id}")
    Optional<Material> getMaterialById(@PathVariable("id") String id);
}
