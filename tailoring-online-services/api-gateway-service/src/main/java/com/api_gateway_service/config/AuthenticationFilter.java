package com.api_gateway_service.config;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
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

    private final ReactiveJwtDecoder reactiveJwtDecoder;
    private static final String BASIC_AUTH_VALUE = String.format("Basic %s", Base64.getEncoder().encodeToString("admin:admin".getBytes()));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return reactiveJwtDecoder.decode(token)
                    .flatMap(jwt -> {
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, authHeader).build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .onErrorResume(e -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        } else {
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, BASIC_AUTH_VALUE).build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }
    }
}
