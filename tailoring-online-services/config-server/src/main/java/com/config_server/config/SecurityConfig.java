package com.config_server.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final Environment environment;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAuthority(environment.getProperty("oauth2.allowed-roles"))
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    jwt.decoder(jwtDecoder);
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                }))
                .oauth2Login(AbstractHttpConfigurer::disable)
                .oauth2Client(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                        .authenticationEntryPoint(((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .build();
    }
}