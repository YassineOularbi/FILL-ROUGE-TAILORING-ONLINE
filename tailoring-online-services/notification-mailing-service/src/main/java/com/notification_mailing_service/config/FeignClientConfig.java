package com.notification_mailing_service.config;

import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Base64;

@Configuration
public class FeignClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientConfig.class);

    String BASIC_AUTH_VALUE = String.format("Basic %s", Base64.getEncoder().encodeToString("admin:admin".getBytes()));

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                logger.debug("Authentification détectée : {}", authentication.getPrincipal());

                if (authentication.getCredentials() instanceof Jwt jwt) {
                    logger.info("JWT détecté, ajout du token à l'en-tête Authorization.");
                    requestTemplate.header("Authorization", String.format("Bearer %s", jwt.getTokenValue()));
                } else if (authentication.getPrincipal() instanceof User) {
                    logger.info("Utilisateur détecté, ajout de l'en-tête Basic Authorization.");
                    requestTemplate.header("Authorization", BASIC_AUTH_VALUE);
                } else {
                    logger.warn("Type d'authentification inconnu, aucun en-tête Authorization ajouté.");
                }
            } else {
                logger.warn("Aucune authentification trouvée dans le SecurityContext.");
            }
        };
    }
}
