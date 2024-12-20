package com.eureka_server.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class JwtKeycloakConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess != null && !resourceAccess.isEmpty()) {
            Map<String, Collection<String>> eurekaResourceClaims = resourceAccess.get("eureka-server-id");

            if (eurekaResourceClaims != null) {
                Collection<String> roles = eurekaResourceClaims.get("roles");
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
