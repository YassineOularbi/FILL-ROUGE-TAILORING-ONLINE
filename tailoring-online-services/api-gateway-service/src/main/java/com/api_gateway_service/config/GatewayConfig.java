package com.api_gateway_service.config;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Base64;

@Configuration
public class GatewayConfig {

    private static final String BASIC_AUTH_VALUE = STR."Basic \{Base64.getEncoder().encodeToString("admin:admin".getBytes())}";

    @Bean
    public DiscoveryClientRouteDefinitionLocator locator(ReactiveDiscoveryClient rd, DiscoveryLocatorProperties dl) {
        return new DiscoveryClientRouteDefinitionLocator(rd, dl);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("localization-shipping-service", r -> r.path("/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.addRequestHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE))
                        .uri("lb://LOCALIZATION-SHIPPING-SERVICE"))

                .route("notification-mailing-service", r -> r.path("/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.addRequestHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE))
                        .uri("lb://NOTIFICATION-MAILING-SERVICE"))

                .route("order-management-service", r -> r.path("/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.addRequestHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE))
                        .uri("lb://ORDER-MANAGEMENT-SERVICE"))

                .route("payment-banking-service", r -> r.path("/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.addRequestHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE))
                        .uri("lb://PAYMENT-BANKING-SERVICE"))

                .route("store-management-service", r -> r.path("/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.addRequestHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE))
                        .uri("lb://STORE-MANAGEMENT-SERVICE"))

                .route("user-management-service", r -> r.path("/v3/api-docs")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.addRequestHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE))
                        .uri("lb://USER-MANAGEMENT-SERVICE"))

                .build();
    }
}

