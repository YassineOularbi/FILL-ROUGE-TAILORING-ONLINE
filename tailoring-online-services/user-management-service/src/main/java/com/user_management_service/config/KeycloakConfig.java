package com.user_management_service.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.core.env.Environment;

@KeycloakConfiguration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final Environment environment;

    public Keycloak getAdminInstance(){
        return KeycloakBuilder.builder()
                .serverUrl(environment.getProperty("keycloak.auth-server-url"))
                .realm(environment.getProperty("keycloak.realm"))
                .clientId(environment.getProperty("keycloak.resource"))
                .clientSecret(environment.getProperty("keycloak.client-secret"))
                .username(environment.getProperty("keycloak.username"))
                .password(environment.getProperty("keycloak.password"))
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    public Keycloak getAuthenticationInstance(String username, String password){
        return KeycloakBuilder.builder()
                .serverUrl(environment.getProperty("keycloak.auth-server-url"))
                .realm(environment.getProperty("keycloak.realm"))
                .clientId(environment.getProperty("keycloak.resource"))
                .clientSecret(environment.getProperty("keycloak.client-secret"))
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    public RealmResource getRealmResource() {
        Keycloak keycloak = getAdminInstance();
        return keycloak.realm(environment.getProperty("keycloak.realm"));
    }

    public CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
