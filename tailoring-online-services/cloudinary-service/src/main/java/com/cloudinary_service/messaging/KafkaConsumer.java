package com.cloudinary_service.messaging;

import com.cloudinary_service.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CloudinaryService cloudinaryService;
    private static final int MAX_RETRIES = 5;

    @Bean
    public Consumer<byte[]> processProfilePicture() {
        return (imageBytes) -> {
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    cloudinaryService.uploadProfilePicture(imageBytes);
                    return;
                } catch (IOException | RuntimeException e) {
                    if (attempt == MAX_RETRIES) {
                        throw new RuntimeException("Failed to process the profile picture after " + attempt + " attempts.", e);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Thread was interrupted during sleep.", ie);
                    }
                }
            }
        };
    }
}
