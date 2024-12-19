package com.eureka_server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(keycloakLogoutHandler())
                        .logoutSuccessUrl("http://localhost:8761/oauth2/authorization/eureka")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "KEYCLOAK_IDENTITY")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/eureka/**", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    jwt.decoder(jwtDecoder);
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                }))
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredSessionStrategy(sessionInformationExpiredStrategy())
                )
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                        .accessDeniedHandler(((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                )
                .build();
    }

    @Bean
    public LogoutSuccessHandler keycloakLogoutHandler() {
        SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
        handler.setDefaultTargetUrl("http://localhost:8080/realms/tailoring-online/protocol/openid-connect/logout?redirect_uri=http://localhost:8761/oauth2/authorization/eureka");
        return (request, response, authentication) -> {
            if (authentication instanceof KeycloakAuthenticationToken) {
                KeycloakSecurityContext securityContext =
                        (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

                Optional.ofNullable(securityContext)
                        .ifPresent(context -> {
                            try {
                                SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
                                securityContextLogoutHandler.logout(request, response, authentication);
                            } catch (Exception ignored) {
                            }
                        });
            }

            handler.onLogoutSuccess(request, response, authentication);
        };
    }

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return event -> {
            HttpServletRequest request = event.getRequest();
            HttpServletResponse response = event.getResponse();

            boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ||
                    (request.getHeader("Accept") != null &&
                            request.getHeader("Accept").contains("application/json"));

            if (isAjax) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"message\": \"Session expired\", \"redirectUrl\": \"/oauth2/authorization/eureka\"}"
                );
            } else {
                response.sendRedirect("/oauth2/authorization/eureka?expired=true");
            }
        };
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}