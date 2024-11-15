package com.user_management_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final StreamBridge streamBridge;
    private static final String TOPIC = "cloudinary-upload-image-request-topic";

    @CircuitBreaker(name = "user-management-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "user-management-service", fallbackMethod = "retryFallback")
    public void sendProfilePicture(MultipartFile profilePicture, String pictureId) {
        byte[] imageBytes;
        try {
            imageBytes = profilePicture.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert multipart file to bytes", e);
        }
        try {
            Message<byte[]> message = MessageBuilder.withPayload(imageBytes)
                    .setHeader("pictureId", pictureId)
                    .build();
            boolean sentSuccessfully = streamBridge.send(TOPIC, message);
            if (!sentSuccessfully) {
                throw new RuntimeException("Failed to send the photo to Kafka.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending the photo to Kafka.", e);
        }
    }

    private void circuitBreakerFallback(MultipartFile profilePicture, String pictureId, Throwable t) {
        throw new RuntimeException("Circuit breaker opened for service cloudinary for file " + profilePicture.getOriginalFilename() + " with ID " + pictureId + " : " + t.getMessage(), t);
    }

    private void retryFallback(MultipartFile profilePicture, String pictureId, Throwable t) {
        throw new RuntimeException("Retry attempts failed for service cloudinary for file " + profilePicture.getOriginalFilename() + " with ID " + pictureId + " : " + t.getMessage(), t);
    }
}