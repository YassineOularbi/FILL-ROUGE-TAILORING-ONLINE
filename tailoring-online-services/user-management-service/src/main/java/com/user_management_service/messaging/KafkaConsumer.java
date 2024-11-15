package com.user_management_service.messaging;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Getter
public class KafkaConsumer {

    private String profilePictureUrl;

    @Bean
    public Consumer<String> processProfilePictureUrl() {
        return this::handleProfilePictureUrl;
    }

    @CircuitBreaker(name = "user-management-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "user-management-service", fallbackMethod = "retryFallback")
    private void handleProfilePictureUrl(String imageUrl) {
        this.profilePictureUrl = imageUrl;
    }

    private void circuitBreakerFallback(String imageUrl, Throwable t) {
        throw new RuntimeException("Circuit breaker opened for service cloudinary for imageUrl: " + imageUrl + " : " + t.getMessage(), t);
    }

    private void retryFallback(String imageUrl, Throwable t) {
        throw new RuntimeException("Retry attempts failed for service cloudinary for imageUrl: " + imageUrl + " : " + t.getMessage(), t);
    }
}