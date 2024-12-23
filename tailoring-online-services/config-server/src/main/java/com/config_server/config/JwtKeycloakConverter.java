package com.config_server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtKeycloakConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final Environment environment;

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Map<String, List<String>> eurekaClaim = jwt.getClaim(environment.getProperty("oauth2.config-claim"));

        if (eurekaClaim != null) {
            List<String> registryRoles = eurekaClaim.get(environment.getProperty("oauth2.fetch-roles"));

            if (registryRoles != null && !registryRoles.isEmpty()) {
                registryRoles.stream()
                        .filter(role -> role.equals(environment.getProperty("oauth2.allowed-roles")))
                        .map(SimpleGrantedAuthority::new)
                        .forEach(grantedAuthorities::add);
            }
        }

        return grantedAuthorities;
    }
}
