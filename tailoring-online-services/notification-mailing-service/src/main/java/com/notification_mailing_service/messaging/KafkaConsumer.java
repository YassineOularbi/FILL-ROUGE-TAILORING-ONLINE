package com.notification_mailing_service.messaging;

import com.notification_mailing_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final EmailService emailService;

    @Bean
    public Consumer<Message<String>> processEmailVerification() {
        return this::handleEmailVerification;
    }

    private void handleEmailVerification(Message<String> message) {
        try {
            String payload = message.getPayload();
            if (payload.isEmpty()) {
                throw new IllegalArgumentException("Message payload is null or empty");
            }
            emailService.sendEmailVerification(payload);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid message payload: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error while processing email verification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
