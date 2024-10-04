package com.api_gateway_service.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class); // Logger ajouté

    private final Environment environment;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        logger.info("Initialisation du ReactiveJwtDecoder avec JWK Set URI : {}", jwkSetUri);
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        logger.info("Initialisation du PasswordEncoder avec BCrypt");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        logger.info("Initialisation du MapReactiveUserDetailsService");
        String username = environment.getProperty("spring.security.user.name");
        String roles = environment.getProperty("spring.security.user.roles");
        logger.debug("Création de l'utilisateur avec le nom d'utilisateur : {} et les rôles : {}", username, roles);

        UserDetails adminUser = User.withUsername(Objects.requireNonNull(username))
                .password(passwordEncoder().encode(Objects.requireNonNull(environment.getProperty("spring.security.user.password"))))
                .roles(Objects.requireNonNull(roles))
                .build();

        logger.info("Utilisateur administrateur créé avec succès");
        return new MapReactiveUserDetailsService(adminUser);
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        logger.info("Initialisation du JwtAuthenticationConverter");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keyCloakAuthConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> keyCloakAuthConverter() {
        logger.info("Initialisation du Keycloak Authentication Converter");
        return new JwtKeycloakConverter();
    }
}
