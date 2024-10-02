package com.localization_shipping_service.config;

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
        logger.info("Configuring security filter chain");

        http.csrf(AbstractHttpConfigurer::disable);
        logger.info("CSRF protection disabled");

        http.cors(Customizer.withDefaults());
        logger.info("CORS enabled");

        http.httpBasic(Customizer.withDefaults());
        logger.info("HTTP Basic authentication enabled");

        http.authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated());
        logger.info("All requests are set to require authentication");

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
            jwt.decoder(jwtDecoder);
            jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
        }));
        logger.info("OAuth2 Resource Server configured with JWT decoder and authentication converter");

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        logger.info("Session management set to stateless");

        return http.build();
    }
}
