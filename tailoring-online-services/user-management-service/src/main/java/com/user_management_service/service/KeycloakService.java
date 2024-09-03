package com.user_management_service.service;

import com.user_management_service.config.KeycloakConfig;
import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.dto.AuthenticationResponse;
import com.user_management_service.dto.UserDto;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakConfig keycloakConfig;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource")
    private String clientId;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    public String addUser(UserDto user) {
        UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(user.getPassword())));
        userRepresentation.setCreatedTimestamp(System.currentTimeMillis());
        userRepresentation.setRealmRoles(List.of(user.getRole().name()));
        userRepresentation.setClientRoles(Map.of(clientId, List.of(user.getRole().name())));
        Response response = usersResource.create(userRepresentation);
        int status = response.getStatus();
        if (status == Response.Status.CREATED.getStatusCode()) {
            String locationHeader = response.getHeaderString("Location");
            return locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
        } else {
            throw new RuntimeException(STR."Failed to create user. HTTP Status: \{status}");
        }
    }

    public List<UserRepresentation> getUser(String userName){
        return keycloakConfig.getInstance(username, password).realm(realm).users().search(userName, true);

    }

    public void updateUser(String userId, UserDto user){
        UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(user.getPassword())));
        userRepresentation.setCreatedTimestamp(System.currentTimeMillis());
        usersResource.get(userId).update(userRepresentation);
    }

    public void deleteUser(String userId){
        keycloakConfig.getInstance(username, password).realm(realm).users().get(userId).remove();
    }

    public void sendVerificationLink(String userId){
        keycloakConfig.getInstance(username, password).realm(realm).users().get(userId).sendVerifyEmail();
    }

    public void sendResetPassword(String userId){
        keycloakConfig.getInstance(username, password).realm(realm).users().get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    public AccessTokenResponse login(AuthenticationRequest authenticationRequest) {
        var keycloak = keycloakConfig.getInstance(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return keycloak.tokenManager().getAccessToken();
    }

}