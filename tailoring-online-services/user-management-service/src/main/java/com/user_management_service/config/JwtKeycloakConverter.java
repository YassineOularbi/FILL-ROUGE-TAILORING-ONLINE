package com.user_management_service.config;

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

        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim(environment.getProperty("oauth2.resource-access-key"));

        if (resourceAccess != null && !resourceAccess.isEmpty()) {
            Map<String, Collection<String>> clientResourceClaims = resourceAccess.get(environment.getProperty("oauth2.client-id"));

            if (clientResourceClaims != null) {
                Collection<String> roles = clientResourceClaims.get(environment.getProperty("oauth2.roles-key"));
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
