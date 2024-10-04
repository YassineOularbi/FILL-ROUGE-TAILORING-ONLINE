package com.api_gateway_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtKeycloakConverter.class);

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        logger.info("Conversion du JWT en GrantedAuthority.");

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess != null && !resourceAccess.isEmpty()) {
            logger.debug("Récupération des rôles à partir du claim 'resource_access' du JWT.");
            resourceAccess.forEach((resource, resourceClaims) -> {
                Collection<String> roles = resourceClaims.get("roles");
                if (roles != null) {
                    logger.debug("Rôles trouvés pour la ressource {} : {}", resource, roles);
                    grantedAuthorities.addAll(
                            roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );
                } else {
                    logger.warn("Aucun rôle trouvé pour la ressource : {}", resource);
                }
            });
        } else {
            logger.warn("Aucun claim 'resource_access' trouvé dans le JWT.");
        }

        logger.info("Conversion des rôles en GrantedAuthority terminée avec succès.");
        return grantedAuthorities;
    }
}
