package com.user_management_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;

@Order(0)
@BootstrapConfiguration
@RequiredArgsConstructor
public class ConfigServerOAuth2 {

    private final Environment environment;
    private final ConfigClientProperties configClientProperties;

    private String getAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(environment.getProperty("spring.cloud.config.oauth2.client"))
                .principal(environment.getProperty("spring.cloud.config.oauth2.client-id"))
                .build();

        OAuth2AuthorizedClientManager authorizedClientManager = createAuthorizedClientManager();
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient != null) {
            return authorizedClient.getAccessToken().getTokenValue();
        } else {
            throw new RuntimeException("Failed to obtain OAuth2 access token");
        }
    }

    private OAuth2AuthorizedClientManager createAuthorizedClientManager() {
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId(environment.getProperty("spring.cloud.config.oauth2.client"))
                .clientId(environment.getProperty("spring.cloud.config.oauth2.client-id"))
                .clientSecret(environment.getProperty("spring.cloud.config.oauth2.client-secret"))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri(environment.getProperty("spring.cloud.config.oauth2.token-uri"))
                .build();

        ClientRegistrationRepository clientRegistrationRepository =
                new InMemoryClientRegistrationRepository(clientRegistration);
        OAuth2AuthorizedClientService authorizedClientService =
                new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);

        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService);
    }

    @Bean
    public ConfigServicePropertySourceLocator configServicePropertySourceLocator() {
        configClientProperties.setHeaders(Map.of("Authorization", "Bearer " + getAccessToken()));
        return new ConfigServicePropertySourceLocator(configClientProperties);
    }
}