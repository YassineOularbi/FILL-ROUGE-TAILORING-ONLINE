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
    @CircuitBreaker(name = "user-management-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "user-management-service", fallbackMethod = "retryFallback")
    public Consumer<String> processProfilePictureUrl() {
        return (imageUrl) -> {
            this.profilePictureUrl = imageUrl;
        };
    }

    private void circuitBreakerFallback(Throwable t) throws RuntimeException {
        throw new RuntimeException("Circuit breaker opened for service cloudinary : " + t.getMessage(), t);
    }

    private void retryFallback(Throwable t) throws RuntimeException {
        throw new RuntimeException("Retry attempts failed for service cloudinary : " + t.getMessage(), t);
    }
}
