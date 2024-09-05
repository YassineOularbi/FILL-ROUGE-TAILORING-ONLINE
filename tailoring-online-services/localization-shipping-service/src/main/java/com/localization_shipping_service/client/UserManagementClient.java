package com.localization_shipping_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-management-service", url = "http://localhost:8081/users")
public interface UserManagementClient {

    @GetMapping("/api/user/get-user-by-id/{id}")
    ResponseEntity<?> getUserById(@PathVariable("id") String id);
}
