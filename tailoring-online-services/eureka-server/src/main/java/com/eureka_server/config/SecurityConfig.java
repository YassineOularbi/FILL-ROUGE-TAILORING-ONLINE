package com.eureka_server.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Initialisation de la chaîne de filtres de sécurité pour Eureka Server.");

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests -> {
                    logger.info("Configuration des autorisations : les requêtes sur /eureka/** sont publiques.");
                    authorizeRequests
                            .requestMatchers("/eureka/**").permitAll()
                            .anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> {
                    logger.info("Configuration de la gestion des sessions : stateless.");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

        logger.info("Chaîne de filtres de sécurité pour Eureka Server configurée avec succès.");
        return http.build();
    }
}
