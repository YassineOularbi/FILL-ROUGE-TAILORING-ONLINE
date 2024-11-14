package com.user_management_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final StreamBridge streamBridge;
    private static final int MAX_RETRIES = 5;

    public void sendProfilePicture(MultipartFile profilePicture) {
        final String topic = "cloudinary-upload-image-request-topic";
        int attempt = 0;
        byte[] imageBytes;
        try {
            imageBytes = profilePicture.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert multipart file to bytes", e);
        }
        while (attempt < MAX_RETRIES) {
            try {
                Message<byte[]> message = MessageBuilder.withPayload(imageBytes).build();
                boolean sentSuccessfully = streamBridge.send(topic, message);
                if (sentSuccessfully) {
                    return;
                }
            } catch (Exception e) {
                attempt++;
                if (attempt >= MAX_RETRIES) {
                    throw new RuntimeException("Error occurred while sending the photo to Kafka after " + attempt + " attempts.", e);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted during sleep.", ie);
            }
            attempt++;
        }
        throw new RuntimeException("Failed to send the photo to Kafka after " + MAX_RETRIES + " attempts.");
    }
}
