package com.config_server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class EurekaOAuth2Config {

    private final Environment environment;

    private String getAccessToken() {

        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(environment.getProperty("EUREKA_OAUTH2_CLIENT"))
                .principal(environment.getProperty("EUREKA_OAUTH2_CLIENT_ID"))
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
                .withRegistrationId(environment.getProperty("EUREKA_OAUTH2_CLIENT"))
                .clientId(environment.getProperty("EUREKA_OAUTH2_CLIENT_ID"))
                .clientSecret(environment.getProperty("EUREKA_OAUTH2_CLIENT_SECRET"))
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri(environment.getProperty("EUREKA_OAUTH2_TOKEN_URI"))
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
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder()
                .customizers(restTemplate -> restTemplate.getInterceptors().add((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                    headers.add("Authorization", "Bearer " + getAccessToken());
                    return execution.execute(request, body);
                }));
    }
}