package com.user_management_service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.userdetails.User;

import java.util.Base64;

@Configuration
public class FeignClientConfig {

    String BASIC_AUTH_VALUE = STR."Basic \{Base64.getEncoder().encodeToString("admin:admin".getBytes())}";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                if (authentication.getCredentials() instanceof Jwt jwt) {
                    System.out.println("Token JWT pour la requête : " + jwt.getTokenValue());
                    requestTemplate.header("Authorization", STR."Bearer \{jwt.getTokenValue()}");
                }
                else if (authentication.getPrincipal() instanceof User) {
                    System.out.println("Basic Auth pour la requête.");
                    requestTemplate.header("Authorization", BASIC_AUTH_VALUE);
                }
                System.out.println("URL Feign request: " + requestTemplate.url());
                System.out.println("Headers: " + requestTemplate.headers());
            }
        };
    }

}
