package com.cloudinary_service.messaging;

import com.cloudinary_service.exception.KafkaProducerException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.util.Collections;

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
                throw new KafkaProducerException("Failed to send profile picture URL to Kafka.", Collections.singletonList("Picture ID: " + pictureId));
            }
        } catch (Exception e) {
            throw new KafkaProducerException("Error occurred while sending the profile picture URL to Kafka.", Collections.singletonList("Error: " + e.getMessage()));
        }
    }

    private void circuitBreakerFallback(String profilePictureUrl, String pictureId, Throwable t) {
        throw new KafkaProducerException("Circuit breaker opened for service user management.", Collections.singletonList("URL: " + profilePictureUrl + ", Picture ID: " + pictureId + ", Reason: " + t.getMessage()));
    }

    private void retryFallback(String profilePictureUrl, String pictureId, Throwable t) {
        throw new KafkaProducerException("Retry attempts failed for service user management.", Collections.singletonList("URL: " + profilePictureUrl + ", Picture ID: " + pictureId + ", Reason: " + t.getMessage()));
    }
}
