package com.api_gateway_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class WebConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    public CorsWebFilter corsWebFilter() {
        logger.info("Initialisation du filtre CORS.");

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200");
        corsConfig.addAllowedOriginPattern("http://localhost:4200");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.setExposedHeaders(List.of("Authorization", "Custom-Header"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        logger.debug("Configuration CORS : Origines autorisées - {}, Méthodes autorisées - {}, En-têtes autorisés - {}",
                corsConfig.getAllowedOrigins(), corsConfig.getAllowedMethods(), corsConfig.getAllowedHeaders());
        logger.debug("Exposed Headers : {}", corsConfig.getExposedHeaders());
        logger.debug("Allow Credentials : {}, Max Age : {}", corsConfig.getAllowCredentials(), corsConfig.getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        logger.info("Filtre CORS initialisé avec succès.");
        return new CorsWebFilter(source);
    }
}
