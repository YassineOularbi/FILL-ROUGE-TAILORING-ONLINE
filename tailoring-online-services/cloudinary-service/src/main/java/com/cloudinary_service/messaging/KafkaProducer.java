package com.cloudinary_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final StreamBridge streamBridge;
    private static final int MAX_RETRIES = 5;


    public void sendProfilePictureUrl(String profilePictureUrl) {
        final String topic = "cloudinary-user-image-response-topic";
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                Message<String> message = MessageBuilder.withPayload(profilePictureUrl).build();
                boolean sentSuccessfully = streamBridge.send(topic, message);

                if (sentSuccessfully) {
                    return;
                }
            } catch (Exception e) {
                attempt++;
                if (attempt >= MAX_RETRIES) {
                    throw new RuntimeException("Failed to send profile picture URL to Kafka after " + attempt + " attempts.", e);
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
        throw new RuntimeException("Failed to send profile picture URL to Kafka after " + MAX_RETRIES + " attempts.");
    }
}
