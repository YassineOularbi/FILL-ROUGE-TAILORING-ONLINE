package com.api_gateway_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OidcKeycloakConverter implements GrantedAuthoritiesMapper {

    private final Environment environment;

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        authorities.forEach(authority -> {
            if (authority instanceof OidcUserAuthority oidcUserAuthority) {
                OidcIdToken idToken = oidcUserAuthority.getIdToken();

                Map<String, List<String>> eurekaClaim = idToken.getClaim(environment.getProperty("oauth2.gateway-claim"));

                if (eurekaClaim != null) {
                    List<String> registryRoles = eurekaClaim.get(environment.getProperty("oauth2.api-roles"));

                    if (registryRoles != null && !registryRoles.isEmpty()) {
                        registryRoles.stream()
                                .filter(role -> role.equals(environment.getProperty("oauth2.allowed-roles")))
                                .map(SimpleGrantedAuthority::new)
                                .forEach(grantedAuthorities::add);
                    }
                }
            }
        });

        return grantedAuthorities;
    }
}
