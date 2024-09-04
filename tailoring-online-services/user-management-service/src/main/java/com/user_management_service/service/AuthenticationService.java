package com.user_management_service.service;

import com.user_management_service.client.NotificationMailingClient;
import com.user_management_service.config.KeycloakConfig;
import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.dto.UserDto;
import com.user_management_service.exception.AuthenticationException;
import com.user_management_service.exception.EmailVerificationException;
import com.user_management_service.exception.KeycloakServiceException;
import com.user_management_service.exception.UserNotFoundException;
import com.user_management_service.model.User;
import com.user_management_service.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;

import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final KeycloakConfig keycloakConfig;
    private final UserRepository userRepository;
    private final NotificationMailingClient notificationMailingClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.resource}")
    private String clientId;


    public AccessTokenResponse login(AuthenticationRequest authenticationRequest) {
        try {
            var keycloak = keycloakConfig.getInstance(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            return keycloak.tokenManager().getAccessToken();
        } catch (Exception e) {
            throw new AuthenticationException("Failed to authenticate user");
        }
    }

    public String addUser(User user) {
        Keycloak keycloak = keycloakConfig.getInstance(username, password);
        UsersResource usersResource = keycloak.realm(realm).users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(false);
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
            throw new KeycloakServiceException(STR."Failed to create user in Keycloak. HTTP Status: \{status}");
        }
    }

    public List<UserRepresentation> getUser(String userName) {
        return keycloakConfig.getInstance(username, password).realm(realm).users().search(userName, true);
    }

    public String getUserEmailById(String userId) {
        UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        return user.getEmail();
    }

    public void updateUser(String userId, UserDto user) {
        UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEnabled(false);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(user.getPassword())));
        userRepresentation.setCreatedTimestamp(System.currentTimeMillis());
        usersResource.get(userId).update(userRepresentation);
    }

    public void deleteUser(String userId) {
        try {
            keycloakConfig.getInstance(username, password).realm(realm).users().get(userId).remove();
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to delete user in Keycloak", e);
        }
    }

    public String sendVerificationCode(String id) {
        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        var email = localUser.getEmail();

        if (email != null && !email.isEmpty()) {
            notificationMailingClient.sendVerificationCode(email);
        }
        return email;
    }

    public String verifyEmail(String id, String code) {
        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        var email = localUser.getEmail();
        if (email != null && !email.isEmpty()) {
            ResponseEntity<String> response = notificationMailingClient.verifyEmail(email, code);
            if (response.getStatusCode().is2xxSuccessful()) {
                UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
                UserRepresentation userRepresentation = new UserRepresentation();
                userRepresentation.setEnabled(true);
                userRepresentation.setEmailVerified(true);
                usersResource.get(id).update(userRepresentation);

                localUser.setEmailVerified(true);
                localUser.setIsVerified(true);
                userRepository.save(localUser);
            } else {
                throw new EmailVerificationException("Invalid verification code");
            }
        } else {
            throw new EmailVerificationException("Error during email verification");
        }
        return email;
    }

    public String sendOtpVerification(String id) {
        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        var email = localUser.getEmail();

        if (email != null && !email.isEmpty()) {
            notificationMailingClient.sendOTPByEmail(email);
        }
        return email;
    }

    public String verifyOtpCode(String id, String code) {
        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        var email = localUser.getEmail();
        if (email != null && !email.isEmpty()) {
            ResponseEntity<String> response = notificationMailingClient.verifyEmail(email, code);
            if (response.getStatusCode().is2xxSuccessful()) {
                localUser.setIs2FAuth(true);
                userRepository.save(localUser);
                return email;
            } else {
                throw new EmailVerificationException("Invalid OTP code");
            }
        } else {
            throw new EmailVerificationException("Error during OTP verification");
        }
    }

}