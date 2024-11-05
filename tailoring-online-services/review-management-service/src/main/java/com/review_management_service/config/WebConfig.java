package com.review_management_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Configuration CORS : ajout de mapping pour les requêtes provenant de http://localhost:9191");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9191")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
                .exposedHeaders("Authorization", "Custom-Header")
                .allowCredentials(true)
                .maxAge(3600);

        logger.info("CORS configuré avec succès pour http://localhost:9191.");
    }
}
