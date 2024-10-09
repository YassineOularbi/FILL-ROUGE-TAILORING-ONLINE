package com.notification_mailing_service.service;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Getter
@Service
public class KafkaConsumerService {

    private boolean userExists = false;

    @KafkaListener(topics = "user-verification-response-topic", groupId = "notification-mailing-group")
    public void listenForUserVerificationResponse(String message) {
        userExists = Boolean.parseBoolean(message);
        System.out.println("User existence verification received: " + userExists);
    }

}

