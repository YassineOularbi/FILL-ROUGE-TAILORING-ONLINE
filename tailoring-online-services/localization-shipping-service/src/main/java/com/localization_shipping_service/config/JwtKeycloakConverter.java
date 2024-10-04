package com.localization_shipping_service.config;

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
        logger.info("Converting JWT to GrantedAuthority collection.");
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        Map<String, Map<String, Collection<String>>> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess != null && !resourceAccess.isEmpty()) {
            logger.info("Resource access claims found in JWT.");
            resourceAccess.forEach((resource, resourceClaims) -> {
                Collection<String> roles = resourceClaims.get("roles");
                if (roles != null) {
                    logger.info("Extracting roles for resource: {}", resource);
                    roles.forEach(role -> {
                        logger.debug("Adding role: {}", role);
                        grantedAuthorities.add(new SimpleGrantedAuthority(role));
                    });
                } else {
                    logger.warn("No roles found for resource: {}", resource);
                }
            });
        } else {
            logger.warn("No resource access claims found in JWT.");
        }

        logger.info("Converted {} roles to GrantedAuthority collection.", grantedAuthorities.size());
        return grantedAuthorities;
    }
}
