package com.api_gateway_service.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdapterConfig {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdapterConfig.class);

    @Bean
    public KeycloakSpringBootConfigResolver springBootConfigResolver(){
        logger.info("Initialisation du bean KeycloakSpringBootConfigResolver pour la configuration Spring Boot de Keycloak.");
        return new KeycloakSpringBootConfigResolver();
    }
}
