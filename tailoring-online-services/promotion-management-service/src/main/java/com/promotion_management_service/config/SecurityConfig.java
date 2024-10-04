package com.promotion_management_service.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Initialisation de la chaîne de filtres de sécurité.");

        http.csrf(AbstractHttpConfigurer::disable);
        logger.debug("Protection CSRF désactivée.");

        http.cors(Customizer.withDefaults());
        logger.debug("Configuration CORS activée par défaut.");

        http.httpBasic(Customizer.withDefaults());
        logger.debug("Authentification HTTP Basic activée.");

        http.authorizeHttpRequests(authorize -> {
            logger.debug("Toutes les requêtes nécessitent une authentification.");
            authorize.anyRequest().authenticated();
        });

        http.oauth2ResourceServer(oauth2 -> {
            logger.debug("Configuration du serveur de ressources OAuth2 avec validation JWT.");
            oauth2.jwt(jwt -> {
                logger.debug("Utilisation de JwtDecoder et JwtAuthenticationConverter.");
                jwt.decoder(jwtDecoder);
                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
            });
        });

        http.sessionManagement(session -> {
            logger.debug("Gestion des sessions configurée : Stateless.");
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        logger.info("Chaîne de filtres de sécurité initialisée avec succès.");
        return http.build();
    }

}
