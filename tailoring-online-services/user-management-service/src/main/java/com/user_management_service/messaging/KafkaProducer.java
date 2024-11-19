package com.user_management_service.messaging;

import com.user_management_service.exception.KafkaProducerException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final StreamBridge streamBridge;

    @CircuitBreaker(name = "user-management-service", fallbackMethod = "circuitBreakerFallback")
    @Retry(name = "user-management-service", fallbackMethod = "retryFallback")
    public void sendProfilePicture(MultipartFile profilePicture, String pictureId) {
        final String TOPIC = "cloudinary-upload-image-request-topic";
        byte[] imageBytes;
        try {
            imageBytes = profilePicture.getBytes();
        } catch (IOException e) {
            throw new KafkaProducerException(e.getMessage(), Collections.singletonList("Failed to convert multipart file to bytes"));
        }
        try {
            Message<byte[]> message = MessageBuilder.withPayload(imageBytes)
                    .setHeader("pictureId", pictureId)
                    .build();
            boolean sentSuccessfully = streamBridge.send(TOPIC, message);
            if (!sentSuccessfully) {
                throw new KafkaProducerException("Failed to send the photo to Kafka.", Collections.singletonList("Failed to send the photo with ID " + pictureId + " to Kafka."));
            }
        } catch (Exception e) {
            throw new KafkaProducerException(e.getMessage(), Collections.singletonList("Failed to send the photo with ID " + pictureId + " to Kafka."));
        }
    }

    private void circuitBreakerFallback(MultipartFile profilePicture, String pictureId, Throwable t) {
        throw new KafkaProducerException(t.getMessage(), Collections.singletonList("Circuit breaker opened for service cloudinary for file " + profilePicture.getOriginalFilename() + " with ID " + pictureId + " : " + t.getMessage()));
    }

    private void retryFallback(MultipartFile profilePicture, String pictureId, Throwable t) {
        throw new KafkaProducerException(t.getMessage(), Collections.singletonList("Retry attempts failed for service cloudinary for file " + profilePicture.getOriginalFilename() + " with ID " + pictureId + " : " + t.getMessage()));
    }
}