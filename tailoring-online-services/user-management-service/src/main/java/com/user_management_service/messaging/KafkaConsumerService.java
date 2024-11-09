package com.user_management_service.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final KafkaProducerService kafkaProducerService;

    @KafkaListener(topics = "user-verification-request-topic", groupId = "user-management-group")
    public void listenForUserVerificationRequest(String email) {

    }
}
