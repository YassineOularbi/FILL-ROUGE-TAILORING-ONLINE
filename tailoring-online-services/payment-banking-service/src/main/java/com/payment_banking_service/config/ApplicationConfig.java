package com.payment_banking_service.config;

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
        logger.info("Initialisation de l'InMemoryUserDetailsManager avec un utilisateur en mémoire.");
        UserDetails user = User.builder()
                .username(Objects.requireNonNull(environment.getProperty("spring.security.user.name"), "Nom d'utilisateur manquant"))
                .password(passwordEncoder().encode(Objects.requireNonNull(environment.getProperty("spring.security.user.password"), "Mot de passe manquant")))
                .roles(Objects.requireNonNull(environment.getProperty("spring.security.user.roles"), "Rôles manquants"))
                .build();
        logger.debug("Utilisateur en mémoire configuré avec succès : {}", user.getUsername());
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        logger.info("Initialisation du JwtDecoder avec JWK Set URI : {}", jwkSetUri);
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Initialisation du PasswordEncoder avec BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        logger.info("Initialisation du JwtAuthenticationConverter.");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keyCloakAuthConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> keyCloakAuthConverter() {
        logger.info("Initialisation du Keycloak JWT Granted Authorities Converter.");
        return new JwtKeycloakConverter();
    }
}