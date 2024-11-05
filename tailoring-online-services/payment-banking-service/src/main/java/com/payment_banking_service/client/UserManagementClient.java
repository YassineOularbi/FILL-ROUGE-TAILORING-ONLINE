package com.payment_banking_service.client;


import com.payment_banking_service.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-management-service", url = "http://localhost:8081")
public interface UserManagementClient {

    @GetMapping("/api/user/get-user-by-id/{id}")
    Optional<User> getUserById(@PathVariable("id") String id);
}
