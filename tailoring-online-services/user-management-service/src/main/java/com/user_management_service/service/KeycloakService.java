package com.user_management_service.service;

import com.user_management_service.config.KeycloakConfig;
import com.user_management_service.dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakConfig keycloakConfig;

    @Value("${keycloak.realm}")
    private String realm;

    public void addUser(RegisterDto user) {
        UsersResource usersResource = keycloakConfig.getInstance().realm(realm).users();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);

        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(user.getPassword())));
        userRepresentation.setCreatedTimestamp(System.currentTimeMillis());
        usersResource.create(userRepresentation);
    }

    public List<UserRepresentation> getUser(String userName){
        return keycloakConfig.getInstance().realm(realm).users().search(userName, true);

    }

    public void updateUser(String userId, RegisterDto userDTO){
        CredentialRepresentation credential = keycloakConfig.createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        keycloakConfig.getInstance().realm(realm).users().get(userId).update(user);
    }

    public void deleteUser(String userId){
        keycloakConfig.getInstance().realm(realm).users().get(userId).remove();
    }

    public void sendVerificationLink(String userId){
        keycloakConfig.getInstance().realm(realm).users().get(userId).sendVerifyEmail();
    }

    public void sendResetPassword(String userId){
        keycloakConfig.getInstance().realm(realm).users().get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

}
