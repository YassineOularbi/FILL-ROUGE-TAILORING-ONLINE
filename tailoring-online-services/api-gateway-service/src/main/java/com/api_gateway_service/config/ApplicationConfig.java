package com.api_gateway_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final Environment environment;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(Objects.requireNonNull(environment.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri"))).build();
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return new OidcKeycloakConverter(environment);
    }

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        return new NettyReactiveWebServerFactory();
    }

    @Bean
    public DiscoveryClientRouteDefinitionLocator locator(ReactiveDiscoveryClient rd, DiscoveryLocatorProperties dl) {
        return new DiscoveryClientRouteDefinitionLocator(rd, dl);
    }

    @Bean
    public GlobalFilter tokenRelayGlobalFilter(TokenRelayGatewayFilterFactory tokenRelayGatewayFilterFactory) {
        return (exchange, chain) -> tokenRelayGatewayFilterFactory.apply().filter(exchange, chain);
    }
}
