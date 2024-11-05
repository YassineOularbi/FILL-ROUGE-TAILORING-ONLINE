package com.notification_mailing_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class VerificationCodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeGenerator.class);

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int DEFAULT_CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public String generateVerificationCode() {
        return generateVerificationCode(DEFAULT_CODE_LENGTH);
    }

    public String generateVerificationCode(int length) {
        logger.info("Génération d'un code de vérification de longueur : {}", length);

        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        String generatedCode = code.toString();
        logger.debug("Code de vérification généré : {}", generatedCode);

        return generatedCode;
    }
}
