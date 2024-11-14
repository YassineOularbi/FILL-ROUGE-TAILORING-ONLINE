package com.cloudinary_service.messaging;

import com.cloudinary_service.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.io.IOException;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CloudinaryService cloudinaryService;

    @Bean
    @CircuitBreaker(name = "cloudinary-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "cloudinary-service", fallbackMethod = "retryFallback")
    public Consumer<byte[]> processProfilePicture() {
        return (imageBytes) -> {
            try {
                cloudinaryService.uploadProfilePicture(imageBytes);
            } catch (IOException | RuntimeException e) {
                throw new RuntimeException("Failed to process the profile picture", e);
            }
        };
    }

    private void circuitBreakerFallback(Throwable t) throws RuntimeException {
        throw new RuntimeException("Circuit breaker opened for service user management : " + t.getMessage(), t);
    }

    private void retryFallback(Throwable t) throws RuntimeException {
        throw new RuntimeException("Retry attempts failed for service user management : " + t.getMessage(), t);
    }
}