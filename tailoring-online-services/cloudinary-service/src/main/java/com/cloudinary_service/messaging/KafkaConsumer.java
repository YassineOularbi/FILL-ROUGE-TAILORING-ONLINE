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

    @Bean
    public Consumer<byte[]> processProfilePicture() {
        return (imageBytes) -> {
            try {
                cloudinaryService.uploadProfilePicture(imageBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
