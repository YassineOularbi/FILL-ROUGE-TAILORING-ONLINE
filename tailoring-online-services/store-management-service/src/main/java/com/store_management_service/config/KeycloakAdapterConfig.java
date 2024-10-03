package com.store_management_service.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdapterConfig {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdapterConfig.class);

    @Bean
    public KeycloakSpringBootConfigResolver springBootConfigResolver() {
        logger.info("Initialisation du KeycloakSpringBootConfigResolver pour utiliser la configuration Spring Boot.");
        return new KeycloakSpringBootConfigResolver();
    }
}
