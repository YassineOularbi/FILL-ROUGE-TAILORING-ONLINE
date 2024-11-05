package com.localization_shipping_service.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collection;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
    private final Environment environment;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public UserDetailsService userDetailsService() {
        String username = Objects.requireNonNull(environment.getProperty("spring.security.user.name"));
        String password = passwordEncoder().encode(Objects.requireNonNull(environment.getProperty("spring.security.user.password")));
        String roles = Objects.requireNonNull(environment.getProperty("spring.security.user.roles"));

        logger.info("Creating UserDetailsService with username: {}", username);

        UserDetails user = User.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        logger.info("Creating JwtDecoder with JWK Set URI: {}", jwkSetUri);
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Creating PasswordEncoder (BCryptPasswordEncoder)");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        logger.info("Creating JwtAuthenticationConverter");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keyCloakAuthConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> keyCloakAuthConverter() {
        logger.info("Creating Keycloak Auth Converter");
        return new JwtKeycloakConverter();
    }
}
