package com.localization_shipping_service.config;

import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Base64;

@Configuration
public class FeignClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientConfig.class);

    String BASIC_AUTH_VALUE = String.format("Basic %s", Base64.getEncoder().encodeToString("admin:admin".getBytes()));

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                logger.info("Adding authorization header for request. Authentication: {}", authentication);

                if (authentication.getCredentials() instanceof Jwt jwt) {
                    logger.info("Using JWT for authorization: {}", jwt.getTokenValue());
                    requestTemplate.header("Authorization", String.format("Bearer %s", jwt.getTokenValue()));
                } else if (authentication.getPrincipal() instanceof User) {
                    logger.info("Using Basic Auth for authorization.");
                    requestTemplate.header("Authorization", BASIC_AUTH_VALUE);
                }
            } else {
                logger.warn("No authentication found. Authorization header will not be added.");
            }
        };
    }
}
