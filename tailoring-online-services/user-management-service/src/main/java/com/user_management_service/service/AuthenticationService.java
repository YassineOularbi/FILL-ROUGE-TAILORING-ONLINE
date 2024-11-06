//package com.user_management_service.service;
//
//import com.user_management_service.client.NotificationMailingClient;
//import com.user_management_service.config.KeycloakConfig;
//import com.user_management_service.dto.AuthenticationRequest;
//import com.user_management_service.dto.UserDto;
//import com.user_management_service.exception.AuthenticationException;
//import com.user_management_service.exception.EmailVerificationException;
//import com.user_management_service.exception.KeycloakServiceException;
//import com.user_management_service.exception.UserNotFoundException;
//import com.user_management_service.model.User;
//import com.user_management_service.repository.UserRepository;
//import jakarta.ws.rs.core.Response;
//import lombok.RequiredArgsConstructor;
//import org.keycloak.admin.client.Keycloak;
//import org.keycloak.admin.client.resource.*;
//
//import org.keycloak.representations.AccessTokenResponse;
//import org.keycloak.representations.idm.ClientRepresentation;
//import org.keycloak.representations.idm.RoleRepresentation;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//    private final KeycloakConfig keycloakConfig;
//    private final UserRepository userRepository;
//    private final NotificationMailingClient notificationMailingClient;
//
//    @Value("${keycloak.realm}")
//    private String realm;
//
//    @Value("${keycloak.username}")
//    private String username;
//
//    @Value("${keycloak.password}")
//    private String password;
//
//    @Value("${keycloak.resource}")
//    private String clientId;
//
//
//    public AccessTokenResponse login(AuthenticationRequest authenticationRequest) {
//        try {
//            var keycloak = keycloakConfig.getInstance(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//            return keycloak.tokenManager().getAccessToken();
//        } catch (Exception e) {
//            throw new AuthenticationException("Failed to authenticate user");
//        }
//    }
//
//    public String addUser(User user) {
//        Logger logger = LoggerFactory.getLogger(getClass());
//
//        logger.info("Starting to add user: {}", user.getUsername());
//        System.out.println("Starting to add user: " + user.getUsername());
//
//        Keycloak keycloak = keycloakConfig.getInstance(username, password);
//        UsersResource usersResource = keycloak.realm(realm).users();
//
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setUsername(user.getUsername());
//        userRepresentation.setFirstName(user.getFirstName());
//        userRepresentation.setLastName(user.getLastName());
//        userRepresentation.setEmail(user.getEmail());
//        userRepresentation.setEnabled(true);
//        userRepresentation.setEmailVerified(false);
//
//        if (user.getPassword() == null || user.getPassword().isEmpty()) {
//            logger.error("Password is null or empty for user: {}", user.getUsername());
//            System.out.println("Error: Password is null or empty for user: " + user.getUsername());
//            throw new IllegalArgumentException("Password cannot be null or empty");
//        }
//        logger.info("Creating password credentials for user: {}", user.getUsername());
//
//        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(user.getPassword())));
//
//        logger.info("Attempting to create user in Keycloak: {}", userRepresentation);
//        System.out.println("Attempting to create user in Keycloak: " + userRepresentation);
//
//        Response response = usersResource.create(userRepresentation);
//        int status = response.getStatus();
//
//        logger.info("Received response status: {}", status);
//        System.out.println("Received response status: " + status);
//
//        if (status == Response.Status.CREATED.getStatusCode()) {
//            String locationHeader = response.getHeaderString("Location");
//            String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
//            logger.info("User created successfully in Keycloak with ID: {}", userId);
//            System.out.println("User created successfully in Keycloak with ID: " + userId);
//
//            ClientRepresentation clientRepresentation = keycloak.realm(realm).clients().findByClientId(clientId).get(0);
//            String clientId = clientRepresentation.getId();
//            ClientResource clientResource = keycloak.realm(realm).clients().get(clientId);
//
//            String roleName = user.getRole().name();
//            logger.info("Assigning role: {} to user ID: {}", roleName, userId);
//            System.out.println("Assigning role: " + roleName + " to user ID: " + userId);
//
//            RoleRepresentation roleRepresentation = clientResource.roles().get(roleName).toRepresentation();
//            RoleScopeResource roleMappingResource = keycloak.realm(realm).users().get(userId).roles().clientLevel(clientId);
//            roleMappingResource.add(Collections.singletonList(roleRepresentation));
//
//            logger.info("Role {} assigned successfully to user ID: {}", roleName, userId);
//            System.out.println("Role " + roleName + " assigned successfully to user ID: " + userId);
//
//            return userId;
//        } else {
//            String errorMessage = response.readEntity(String.class);
//            logger.error("Failed to create user in Keycloak. HTTP Status: {}. Error: {}", status, errorMessage);
//            System.out.println("Failed to create user in Keycloak. HTTP Status: " + status + ". Error: " + errorMessage);
//            throw new KeycloakServiceException(String.format("Failed to create user in Keycloak. HTTP Status: %s. Error: %s", status, errorMessage));
//        }
//    }
//
//    public List<UserRepresentation> getUser(String userName) {
//        return keycloakConfig.getInstance(username, password).realm(realm).users().search(userName, true);
//    }
//
//    public String getUserEmailById(String userId) {
//        UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
//        UserRepresentation user = usersResource.get(userId).toRepresentation();
//        return user.getEmail();
//    }
//
//    public void updateUser(String userId, UserDto user) {
//        UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setUsername(user.getUsername());
//        userRepresentation.setFirstName(user.getFirstName());
//        userRepresentation.setLastName(user.getLastName());
//        userRepresentation.setEmail(user.getEmail());
//        userRepresentation.setEnabled(true);
//        userRepresentation.setEmailVerified(user.getEmailVerified());
//        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(user.getPassword())));
//        userRepresentation.setCreatedTimestamp(System.currentTimeMillis());
//        usersResource.get(userId).update(userRepresentation);
//    }
//
//    public void deleteUser(String userId) {
//        try {
//            keycloakConfig.getInstance(username, password).realm(realm).users().get(userId).remove();
//        } catch (Exception e) {
//            throw new KeycloakServiceException("Failed to delete user in Keycloak", e);
//        }
//    }
//
//    public String sendVerificationCode(String id) {
//        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//        var email = localUser.getEmail();
//        if (email != null && !email.isEmpty()) {
//            notificationMailingClient.sendVerificationCode(email);
//        }
//        return email;
//    }
//
//    public String verifyEmail(String id, String code) {
//        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//        var email = localUser.getEmail();
//        if (email != null && !email.isEmpty()) {
//            ResponseEntity<String> response = notificationMailingClient.verifyEmail(email, code);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                UsersResource usersResource = keycloakConfig.getInstance(username, password).realm(realm).users();
//                UserRepresentation userRepresentation = new UserRepresentation();
//                userRepresentation.setEnabled(true);
//                userRepresentation.setEmailVerified(true);
//                usersResource.get(id).update(userRepresentation);
//
//                localUser.setEmailVerified(true);
//                localUser.setIsVerified(true);
//                userRepository.save(localUser);
//            } else {
//                throw new EmailVerificationException("Invalid verification code");
//            }
//        } else {
//            throw new EmailVerificationException("Error during email verification");
//        }
//        return email;
//    }
//
//    public String sendOtpVerification(String id) {
//        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//        var email = localUser.getEmail();
//
//        if (email != null && !email.isEmpty()) {
//            notificationMailingClient.sendOTPByEmail(email);
//        }
//        return email;
//    }
//
//    public String verifyOtpCode(String id, String code) {
//        var localUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//        var email = localUser.getEmail();
//        if (email != null && !email.isEmpty()) {
//            ResponseEntity<String> response = notificationMailingClient.verifyEmail(email, code);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                localUser.setIs2FAuth(true);
//                userRepository.save(localUser);
//                return email;
//            } else {
//                throw new EmailVerificationException("Invalid OTP code");
//            }
//        } else {
//            throw new EmailVerificationException("Error during OTP verification");
//        }
//    }
//
//}