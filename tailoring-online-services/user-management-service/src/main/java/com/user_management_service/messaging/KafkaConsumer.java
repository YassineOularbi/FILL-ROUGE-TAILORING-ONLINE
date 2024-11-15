package com.user_management_service.messaging;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final Map<String, String> profilePictureUrls = new ConcurrentHashMap<>();

    @Bean
    public Consumer<Message<String>> processProfilePictureUrl() {
        return this::handleProfilePictureUrl;
    }

    @CircuitBreaker(name = "user-management-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "user-management-service", fallbackMethod = "retryFallback")
    private void handleProfilePictureUrl(Message<String> message) {
        try {
            String pictureId = message.getHeaders().get("pictureId", String.class);
            if (pictureId == null) {
                throw new RuntimeException("pictureId header is missing in the message");
            }

            String profilePictureUrl = message.getPayload();
            profilePictureUrls.put(pictureId, profilePictureUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process profile picture message: " + message.getPayload(), e);
        }
    }

    private void circuitBreakerFallback(Message<String> message, Throwable t) {
        String pictureId = message.getHeaders().get("pictureId", String.class);
        throw new RuntimeException("Circuit breaker opened for service cloudinary for picture ID " + pictureId + " in message: " + message.getPayload() + " : " + t.getMessage(), t);
    }

    private void retryFallback(Message<String> message, Throwable t) {
        String pictureId = message.getHeaders().get("pictureId", String.class);
        throw new RuntimeException("Retry attempts failed for service cloudinary for picture ID " + pictureId + " in message: " + message.getPayload() + " : " + t.getMessage(), t);
    }

    public String getProfilePictureUrl(String id) {
        return profilePictureUrls.get(id);
    }
}