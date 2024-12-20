package com.user_management_service.config;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public class ConfigServerOAuth2 implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            String client = environment.getProperty("spring.cloud.config.oauth2.client");
            String clientId = environment.getProperty("spring.cloud.config.oauth2.client-id");
            String clientSecret = environment.getProperty("spring.cloud.config.oauth2.client-secret");
            String tokenUri = environment.getProperty("spring.cloud.config.oauth2.token-uri");
            String configServerUrl = environment.getProperty("spring.cloud.config.uri");
            String applicationName = environment.getProperty("spring.application.name");
            String profile = environment.getProperty("spring.profiles.active");

            ClientRegistration clientRegistration = ClientRegistration
                    .withRegistrationId(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .tokenUri(tokenUri)
                    .build();

            ClientRegistrationRepository clientRegistrationRepository = new InMemoryClientRegistrationRepository(clientRegistration);
            OAuth2AuthorizedClientService authorizedClientService = new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
            OAuth2AuthorizedClientManager authorizedClientManager = createAuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(client)
                    .principal(applicationName)
                    .build();

            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

            if (authorizedClient != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue());

                String configUrl = String.format("%s/%s/%s", configServerUrl, applicationName, profile);

                ResponseEntity<String> response = restTemplate.exchange(
                        configUrl,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    Properties properties = parseProperties(response.getBody());
                    PropertiesPropertySource propertySource = new PropertiesPropertySource("configServer", properties);
                    environment.getPropertySources().addLast(propertySource);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to fetch configuration from config server", e);
        }
    }

    private OAuth2AuthorizedClientManager createAuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                        OAuth2AuthorizedClientService authorizedClientService) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    private Properties parseProperties(String responseBody) {
        Properties properties = new Properties();
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray propertySources = jsonResponse.getJSONArray("propertySources");

        if (!propertySources.isEmpty()) {
            JSONObject firstPropertySource = propertySources.getJSONObject(0);
            JSONObject source = firstPropertySource.getJSONObject("source");

            for (String key : source.keySet()) {
                properties.put(key, source.get(key).toString());
            }
        }
        return properties;
    }
}
