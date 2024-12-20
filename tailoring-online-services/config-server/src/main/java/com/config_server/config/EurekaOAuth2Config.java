package com.config_server.config;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.configuration.SSLContextFactory;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.cloud.netflix.eureka.http.RestTemplateDiscoveryClientOptionalArgs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

@Configuration
public class EurekaOAuth2Config {

    private static final Logger logger = LoggerFactory.getLogger(EurekaOAuth2Config.class);

    private String getAccessToken() {
        logger.info("Attempting to retrieve OAuth2 access token");

        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("eureka-server")
                .principal("eureka-server-id")
                .build();

        OAuth2AuthorizedClientManager authorizedClientManager = createAuthorizedClientManager();
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient != null) {
            logger.info("OAuth2 access token retrieved successfully");
            return authorizedClient.getAccessToken().getTokenValue();
        } else {
            logger.error("Failed to obtain OAuth2 access token");
            throw new RuntimeException("Failed to obtain OAuth2 access token");
        }
    }

    private OAuth2AuthorizedClientManager createAuthorizedClientManager() {
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("eureka-server")
                .clientId("eureka-server-id")
                .clientSecret("8dRSc1Sa4tne6BpFba13NYNKJ854T7Yr")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .tokenUri("http://localhost:8080/realms/tailoring-online/protocol/openid-connect/token")
                .build();

        ClientRegistrationRepository clientRegistrationRepository =
                new InMemoryClientRegistrationRepository(clientRegistration);
        OAuth2AuthorizedClientService authorizedClientService =
                new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService);
    }

    @Bean
    public RestTemplateBuilder eurekaRestTemplate() {
        return new RestTemplateBuilder()
                .additionalInterceptors(new TokenInterceptor());
    }

    private class TokenInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("Authorization", "Bearer " + getAccessToken());
            return execution.execute(request, body);
        }
    }
}