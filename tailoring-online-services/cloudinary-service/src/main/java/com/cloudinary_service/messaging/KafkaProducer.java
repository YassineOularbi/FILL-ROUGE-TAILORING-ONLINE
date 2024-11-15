package com.cloudinary_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final StreamBridge streamBridge;
    private static final String TOPIC = "cloudinary-user-image-response-topic";

    @CircuitBreaker(name = "cloudinary-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "cloudinary-service", fallbackMethod = "retryFallback")
    public void sendProfilePictureUrl(String profilePictureUrl, String pictureId) {
        try {
            Message<String> message = MessageBuilder.withPayload(profilePictureUrl)
                    .setHeader("pictureId", pictureId)
                    .build();
            boolean sentSuccessfully = streamBridge.send(TOPIC, message);
            if (!sentSuccessfully) {
                throw new RuntimeException("Failed to send profile picture URL to Kafka.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending the profile picture URL to Kafka.", e);
        }
    }

    private void circuitBreakerFallback(String profilePictureUrl, String pictureId, Throwable t) {
        throw new RuntimeException("Circuit breaker opened for service user management for URL: " + profilePictureUrl + " with ID: " + pictureId + " : " + t.getMessage(), t);
    }

    private void retryFallback(String profilePictureUrl, String pictureId, Throwable t) {
        throw new RuntimeException("Retry attempts failed for service user management for URL: " + profilePictureUrl + " with ID: " + pictureId + " : " + t.getMessage(), t);
    }
}