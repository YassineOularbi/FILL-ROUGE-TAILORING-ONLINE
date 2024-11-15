package com.cloudinary_service.messaging;

import com.cloudinary_service.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
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
    public Consumer<Message<byte[]>> processProfilePicture() {
        return this::handleProfilePicture;
    }

    @CircuitBreaker(name = "cloudinary-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "cloudinary-service", fallbackMethod = "retryFallback")
    private void handleProfilePicture(Message<byte[]> message) {
        byte[] imageBytes = message.getPayload();
        String pictureId = message.getHeaders().get("pictureId", String.class);

        try {
            cloudinaryService.uploadProfilePicture(imageBytes, pictureId);
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Failed to process the profile picture with ID " + pictureId, e);
        }
    }

    private void circuitBreakerFallback(Message<byte[]> message, Throwable t) {
        String pictureId = message.getHeaders().get("pictureId", String.class);
        throw new RuntimeException("Circuit breaker opened for service cloudinary for picture ID " + pictureId + ": " + t.getMessage(), t);
    }

    private void retryFallback(Message<byte[]> message, Throwable t) {
        String pictureId = message.getHeaders().get("pictureId", String.class);
        throw new RuntimeException("Retry attempts failed for service cloudinary for picture ID " + pictureId + ": " + t.getMessage(), t);
    }
}