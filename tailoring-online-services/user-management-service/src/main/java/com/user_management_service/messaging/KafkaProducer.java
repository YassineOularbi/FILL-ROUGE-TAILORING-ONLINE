package com.user_management_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final StreamBridge streamBridge;

    public void sendProfilePicture(MultipartFile profilePicture) throws IOException {
        final String topic = "cloudinary-upload-image-request-topic";
        try {
            byte[] imageBytes = profilePicture.getBytes();
            Message<byte[]> message = MessageBuilder.withPayload(imageBytes).build();
            streamBridge.send(topic, message);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while sending the photo to Kafka", e);
        }
    }

}