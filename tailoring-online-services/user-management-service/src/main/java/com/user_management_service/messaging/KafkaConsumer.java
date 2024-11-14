package com.user_management_service.messaging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Getter
public class KafkaConsumer {

    private String profilePictureUrl;

    @Bean
    public Consumer<String> processProfilePictureUrl() {
        return (imageUrl) -> {
            this.profilePictureUrl = imageUrl;
        };
    }
}
