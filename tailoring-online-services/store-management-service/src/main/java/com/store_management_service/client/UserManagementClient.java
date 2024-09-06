package com.store_management_service.client;


import com.store_management_service.model.Tailor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-management-service", url = "http://localhost:8081")
public interface UserManagementClient {

    @GetMapping("/api/tailor/get-tailor-by-id/{id}")
    Optional<Tailor> getTailorById(@PathVariable("id") String id);
}
