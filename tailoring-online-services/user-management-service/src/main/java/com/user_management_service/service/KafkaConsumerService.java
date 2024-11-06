//package com.user_management_service.service;
//
//import com.user_management_service.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaConsumerService {
//
//    private final UserRepository userRepository;
//    private final KafkaProducerService kafkaProducerService;
//
//    @KafkaListener(topics = "user-verification-request-topic", groupId = "user-management-group")
//    public void listenForUserVerificationRequest(String email) {
//        boolean userExists = userRepository.existsByEmail(email);
//        kafkaProducerService.sendUserVerificationResponse(userExists);
//    }
//}
