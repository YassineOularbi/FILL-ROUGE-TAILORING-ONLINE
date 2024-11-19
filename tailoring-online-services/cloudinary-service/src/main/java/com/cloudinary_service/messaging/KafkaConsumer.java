package com.cloudinary_service.messaging;

import com.cloudinary_service.exception.KafkaConsumerException;
import com.cloudinary_service.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.io.IOException;
import java.util.Collections;
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
        } catch (IOException e) {
            throw new KafkaConsumerException("IO error occurred while processing profile picture with ID: " + pictureId, Collections.singletonList("Cause: " + e.getMessage()));
        } catch (RuntimeException e) {
            throw new KafkaConsumerException("Failed to process profile picture with ID: " + pictureId, Collections.singletonList("Cause: " + e.getMessage()));
        }
    }

    private void circuitBreakerFallback(Message<byte[]> message, Throwable t) {
        String pictureId = message.getHeaders().get("pictureId", String.class);
        throw new KafkaConsumerException("Circuit breaker opened for Cloudinary service.", Collections.singletonList("Reason: " + t.getMessage()));
    }

    private void retryFallback(Message<byte[]> message, Throwable t) {
        String pictureId = message.getHeaders().get("pictureId", String.class);
        throw new KafkaConsumerException("Retry attempts failed for Cloudinary service.", Collections.singletonList("Reason: " + t.getMessage()));
    }
}
