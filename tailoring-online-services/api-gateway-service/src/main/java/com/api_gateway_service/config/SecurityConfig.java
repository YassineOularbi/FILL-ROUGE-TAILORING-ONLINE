package com.api_gateway_service.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.core.convert.converter.Converter;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final ReactiveJwtDecoder reactiveJwtDecoder;
    private final Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        logger.info("Initialisation de la chaîne de filtres de sécurité Web.");

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(authorize -> {
                    logger.debug("Configuration des règles d'autorisation : toutes les requêtes sont autorisées.");
                    authorize.anyExchange().permitAll();
                })
                .oauth2ResourceServer(oauth2 -> {
                    logger.debug("Configuration du serveur de ressources OAuth2 avec JWT.");
                    oauth2.jwt(jwt -> {
                        logger.debug("Utilisation du JWT Decoder et du JWT Authentication Converter pour la validation des tokens.");
                        jwt.jwtDecoder(reactiveJwtDecoder);
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                    });
                })
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());

        logger.info("Chaîne de filtres de sécurité Web initialisée avec succès.");
        return http.build();
    }

}
