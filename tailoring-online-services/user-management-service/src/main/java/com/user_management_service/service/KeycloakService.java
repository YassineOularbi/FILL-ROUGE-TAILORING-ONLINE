package com.user_management_service.service;

import com.user_management_service.config.KeycloakConfig;
import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.dto.UserDto;
import com.user_management_service.model.User;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.UsersResource;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakConfig keycloakConfig;

    private final EmailService emailService;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.resource}")
    private String clientId;

    public String addUser(User user) {
        Keycloak keycloak = keycloakConfig.getInstance(username, password);
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(user.getPassword())));
        userRepresentation.setCreatedTimestamp(System.currentTimeMillis());
        Response response = usersResource.create(userRepresentation);
        int status = response.getStatus();

        if (status == Response.Status.CREATED.getStatusCode()) {
            String locationHeader = response.getHeaderString("Location");
            String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
            ClientRepresentation clientRepresentation = keycloak.realm(realm).clients().findByClientId(clientId).getFirst();
            String clientId = clientRepresentation.getId();
            ClientResource clientResource = keycloak.realm(realm).clients().get(clientId);
            String roleName = user.getRole().name();
            RoleRepresentation roleRepresentation = clientResource.roles().get(roleName).toRepresentation();
            RoleScopeResource roleMappingResource = keycloak.realm(realm).users().get(userId).roles().clientLevel(clientId);
            roleMappingResource.add(Collections.singletonList(roleRepresentation));

            return userId;
        } else {
            throw new RuntimeException("Failed to create user. HTTP Status: " + status);
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

    public void sendVerificationLink(String userId) {
        Keycloak keycloak = keycloakConfig.getInstance(username, password);
//        String email = keycloak.realm(realm).users().get(userId).toRepresentation().getEmail();
//        keycloak.realm(realm).users().get(userId).sendVerifyEmail();
        emailService.sendEmail("Tailoring-online@outlook.com", "Verify Your Email", "Click the link to verify your email: [verification link]");
    }

    public void sendResetPassword(String userId) {
        Keycloak keycloak = keycloakConfig.getInstance(username, password);
        String email = keycloak.realm(realm).users().get(userId).toRepresentation().getEmail();
        keycloak.realm(realm).users().get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
        emailService.sendEmail(email, "Reset Your Password", "Click the link to reset your password: [reset password link]");
    }

    public AccessTokenResponse login(AuthenticationRequest authenticationRequest) {
        var keycloak = keycloakConfig.getInstance(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return keycloak.tokenManager().getAccessToken();
    }

}