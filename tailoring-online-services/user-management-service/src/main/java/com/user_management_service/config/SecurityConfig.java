package com.user_management_service.config;


import com.user_management_service.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth/**", "/api/customer/register/**", "/api/admin/register/**", "/api/tailor/register/**").permitAll()
                                .requestMatchers("/api/admin/**").hasAuthority(Role.ADMIN.name())
                                .requestMatchers("/api/tailor/**").hasAnyAuthority(Role.TAILOR.name(), Role.ADMIN.name())
                                .requestMatchers("/api/customer/**").hasAnyAuthority(Role.CUSTOMER.name(), Role.ADMIN.name())
                                .requestMatchers("api/user/**").hasAuthority(Role.ADMIN.name())
                                .anyRequest().permitAll());
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
