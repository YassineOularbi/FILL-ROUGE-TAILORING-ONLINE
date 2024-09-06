//package com.store_management_service.config;
//
//import feign.RequestInterceptor;
//import jakarta.ws.rs.core.HttpHeaders;
//import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//
//@Configuration
//public class FeignConfig {
//
//    @Bean
//    public RequestInterceptor requestInterceptor(OAuth2AuthorizedClientService authorizedClientService) {
//        return requestTemplate -> {
//            OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
//                    authentication.getAuthorizedClientRegistrationId(),
//                    authentication.getName()
//            );
//            String token = authorizedClient.getAccessToken().getTokenValue();
//            requestTemplate.header(HttpHeaders.AUTHORIZATION, STR."Bearer \{token}");
//        };
//    }
//}