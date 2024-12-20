package com.eureka_server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtKeycloakConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final Environment environment;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim(environment.getProperty("OAUTH2_RESOURCE_ACCESS_KEY"));

        if (resourceAccess != null && !resourceAccess.isEmpty()) {
            Map<String, Collection<String>> clientResourceClaims = resourceAccess.get(environment.getProperty("OAUTH2_CLIENT_ID"));

            if (clientResourceClaims != null) {
                Collection<String> roles = clientResourceClaims.get(environment.getProperty("OAUTH2_ROLES_KEY"));
                if (roles != null && !roles.isEmpty()) {
                    grantedAuthorities.addAll(
                            roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );
                }
            }
        }

        return grantedAuthorities;
    }
}
