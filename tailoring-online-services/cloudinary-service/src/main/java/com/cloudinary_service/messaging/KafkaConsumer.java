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
    public Consumer<byte[]> processProfilePicture() {
        return this::handleProfilePicture;
    }

    @CircuitBreaker(name = "cloudinary-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "cloudinary-service", fallbackMethod = "retryFallback")
    private void handleProfilePicture(byte[] imageBytes) {
        try {
            cloudinaryService.uploadProfilePicture(imageBytes);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to process the profile picture", e);
        }
    }

    private void circuitBreakerFallback(byte[] imageBytes, Throwable t) {
        throw new RuntimeException("Circuit breaker opened for service cloudinary: " + t.getMessage(), t);
    }

    private void retryFallback(byte[] imageBytes, Throwable t) {
        throw new RuntimeException("Retry attempts failed for service cloudinary: " + t.getMessage(), t);
    }
}