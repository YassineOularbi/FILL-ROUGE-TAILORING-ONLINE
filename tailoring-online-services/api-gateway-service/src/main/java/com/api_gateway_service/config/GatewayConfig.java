package com.api_gateway_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    @Bean
    public DiscoveryClientRouteDefinitionLocator locator(ReactiveDiscoveryClient rd, DiscoveryLocatorProperties dl) {
        logger.info("Initialisation du DiscoveryClientRouteDefinitionLocator avec ReactiveDiscoveryClient et DiscoveryLocatorProperties.");

        if (rd != null) {
            logger.debug("ReactiveDiscoveryClient fourni : {}", rd.getClass().getSimpleName());
        } else {
            logger.warn("ReactiveDiscoveryClient est null.");
        }

        if (dl != null) {
            logger.debug("DiscoveryLocatorProperties fourni : {}", dl);
        } else {
            logger.warn("DiscoveryLocatorProperties est null.");
        }

        assert rd != null;
        assert dl != null;
        DiscoveryClientRouteDefinitionLocator locator = new DiscoveryClientRouteDefinitionLocator(rd, dl);
        logger.info("DiscoveryClientRouteDefinitionLocator initialisé avec succès.");

        return locator;
    }

}
