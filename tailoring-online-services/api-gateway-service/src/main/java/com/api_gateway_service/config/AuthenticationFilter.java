package com.api_gateway_service.config;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class); // Logger ajouté
    private final ReactiveJwtDecoder reactiveJwtDecoder;
    private static final String BASIC_AUTH_VALUE = String.format("Basic %s", Base64.getEncoder().encodeToString("admin:admin".getBytes()));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            logger.info("Authentification Bearer détectée, vérification du token JWT");
            String token = authHeader.substring(7);

            return reactiveJwtDecoder.decode(token)
                    .flatMap(jwt -> {
                        logger.info("JWT valide, ajout de l'en-tête d'autorisation à la requête");
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, authHeader).build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(e -> {
                        logger.error("Erreur lors de la validation du token JWT : {}", e.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        } else {
            logger.warn("Aucun Bearer token trouvé, utilisation de l'authentification Basic par défaut");
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE).build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }
    }
}
