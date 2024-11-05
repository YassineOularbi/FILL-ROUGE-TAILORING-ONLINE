package com.user_management_service.client;

import com.user_management_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "notification-mailing-service", url = "http://localhost:9191/NOTIFICATION-MAILING-SERVICE", configuration = FeignClientConfig.class)
public interface NotificationMailingClient {

    @GetMapping("/api/email/send-verification-code/{email}")
    String sendVerificationCode(@PathVariable("email") String email);

    @GetMapping("/api/email/send-otp-verification/{email}")
    String sendOTPByEmail(@PathVariable("email") String email);

    @GetMapping("/api/email/verify-code/{email}&{code}")
    ResponseEntity<String> verifyEmail(@PathVariable("email") String email, @PathVariable("code") String code);
}
