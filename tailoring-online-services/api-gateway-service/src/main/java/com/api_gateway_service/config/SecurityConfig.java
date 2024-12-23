package com.api_gateway_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.session.InMemoryReactiveSessionRegistry;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerMaximumSessionsExceededHandler;
import org.springframework.security.web.server.authentication.SessionLimit;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

import java.net.URI;
import java.util.*;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveJwtDecoder reactiveJwtDecoder;
    private final GrantedAuthoritiesMapper grantedAuthoritiesMapper;
    private final Environment environment;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutHandler(keycloakLogoutHandler())
                        .logoutSuccessHandler(keycloakLogoutSuccessHandler())
                )
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/USER-MANAGEMENT-SERVICE/api/v1/auth/**").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").hasAuthority(Objects.requireNonNull(environment.getProperty("oauth2.allowed-roles")))
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(reactiveJwtDecoder))
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .sessionManagement(sessionManagementSpec -> sessionManagementSpec
                        .concurrentSessions(concurrentSessionsSpec -> concurrentSessionsSpec
                                .maximumSessions(SessionLimit.of(1))
                                .sessionRegistry(inMemoryReactiveSessionRegistry())
                                .maximumSessionsExceededHandler(maximumSessionsExceededHandler())
                        )
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .anonymous(ServerHttpSecurity.AnonymousSpec::disable)
                .build();
    }

    @Bean
    public ServerLogoutHandler keycloakLogoutHandler() {
        return (exchange, authentication) -> {
            exchange.getExchange().getResponse().getCookies().clear();
            return exchange.getExchange().getSession().flatMap(webSession -> {
                webSession.getAttributes().clear();
                return webSession.invalidate();
            }).then(Mono.defer(() -> {
                exchange.getExchange().getResponse().setStatusCode(null);
                exchange.getExchange().getResponse()
                        .getHeaders().setLocation(URI.create(Objects.requireNonNull(environment.getProperty("oauth2.logout-success-url"))));
                return Mono.empty();
            }));
        };
    }

    @Bean
    public ServerLogoutSuccessHandler keycloakLogoutSuccessHandler() {
        RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
        handler.setLogoutSuccessUrl(URI.create(Objects.requireNonNull(environment.getProperty("oauth2.logout-url"))));
        return handler;
    }

    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();
        return userRequest -> delegate.loadUser(userRequest)
                .map(oidcUser -> {
                    var mappedAuthorities = grantedAuthoritiesMapper.mapAuthorities(oidcUser.getAuthorities());
                    return new DefaultOidcUser(
                            mappedAuthorities,
                            oidcUser.getIdToken(),
                            oidcUser.getUserInfo()
                    );
                });
    }

    @Bean
    public InMemoryReactiveSessionRegistry inMemoryReactiveSessionRegistry() {
        return new InMemoryReactiveSessionRegistry();
    }

    @Bean
    public ServerMaximumSessionsExceededHandler maximumSessionsExceededHandler() {
        return context -> {
            ServerWebExchange exchange = context.getCurrentSession().getAttribute("SPRING_SECURITY_CONTEXT");
            if (exchange != null) {
                exchange.getResponse().setStatusCode(HttpStatus.CONFLICT);
                return exchange.getResponse().writeWith(Mono.just(
                        exchange.getResponse().bufferFactory().wrap(
                                "{\"message\": \"Maximum de sessions atteint\"}".getBytes()
                        )
                ));
            }
            return Mono.empty();
        };
    }

    @Bean
    public ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) -> {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().writeWith(Mono.just(
                    exchange.getResponse().bufferFactory().wrap(
                            "{\"message\": \"Access Denied\"}".getBytes()
                    )
            ));
        };
    }
}