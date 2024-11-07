package com.user_management_service.service;

import com.user_management_service.config.KeycloakConfig;
import com.user_management_service.dto.AuthenticationRequest;
import com.user_management_service.dto.CreateCustomerDto;
import com.user_management_service.exception.AuthenticationException;
import com.user_management_service.exception.KeycloakException;
import com.user_management_service.exception.UserAlreadyExistsException;
import com.user_management_service.mapper.CustomerMapper;
import com.user_management_service.validation.CreateGroup;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final KeycloakConfig keycloakConfig;
    private final CustomerMapper customerMapper;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    public AccessTokenResponse login(@Validated AuthenticationRequest authenticationRequest) {
        try {
            var keycloak = keycloakConfig.getInstance(authenticationRequest.username(), authenticationRequest.password());
            return keycloak.tokenManager().getAccessToken();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }


    public void registerCustomer(@Validated(CreateGroup.class) CreateCustomerDto createCustomerDto) {
        var customer = customerMapper.toCreateEntity(createCustomerDto);
        logger.info("Starting to add user: {}", customer.getUsername());
        Keycloak keycloak = keycloakConfig.getInstance(username, password);
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> existingUsersByUsername = usersResource.search(customer.getUsername(), true);
        if (!existingUsersByUsername.isEmpty()) {
            List<String> details = List.of("User with username " + customer.getUsername() + " already exists.");
            throw new UserAlreadyExistsException("User with username " + customer.getUsername() + " already exists.", details);
        }
        List<UserRepresentation> existingUsersByEmail = usersResource.search(null, null, null, customer.getEmail(), 0, 1);
        if (!existingUsersByEmail.isEmpty()) {
            List<String> details = List.of("User with email " + customer.getEmail() + " already exists.");
            throw new UserAlreadyExistsException("User with email " + customer.getEmail() + " already exists.", details);
        }
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(customer.getUsername());
        userRepresentation.setFirstName(customer.getFirstName());
        userRepresentation.setLastName(customer.getLastName());
        userRepresentation.setEmail(customer.getEmail());
        userRepresentation.setEnabled(false);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setTotp(false);
        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(customer.getPassword())));
        userRepresentation.setGroups(Collections.singletonList(customer.getRole().name()));
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phoneNumber", Collections.singletonList(customer.getPhoneNumber()));
        attributes.put("dateOfBirth", Collections.singletonList(customer.getDateOfBirth().toString()));
        attributes.put("gender", Collections.singletonList(customer.getGender().name()));
        attributes.put("lastLogin", Collections.singletonList(LocalDateTime.now().toString()));
        attributes.put("languagePreference", Collections.singletonList(customer.getLanguagePreference().name()));
        attributes.put("status", Collections.singletonList(customer.getStatus().name()));
        attributes.put("profilePicture", Collections.singletonList(customer.getProfilePicture()));
        attributes.put("loyaltyPoints", Collections.singletonList(customer.getLoyaltyPoints().toString()));
        userRepresentation.setAttributes(attributes);
        logger.info("Attempting to create user in Keycloak: {}", userRepresentation);
        try {
            Response response = usersResource.create(userRepresentation);
            String responseBody = response.readEntity(String.class);
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                logger.info("User {} created successfully in Keycloak", customer.getUsername());
//                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//                String groupName = customer.getRole().name();
//                var groupsResource = keycloak.realm(realm).groups();
//                List<GroupRepresentation> groups = groupsResource.groups();
//                GroupRepresentation group = groups.stream()
//                        .filter(g -> g.getName().equals(groupName))
//                        .findFirst()
//                        .orElseThrow(() -> new KeycloakException("Group not found", List.of("Group " + groupName + " does not exist in Keycloak")));
//                usersResource.get(userId).joinGroup(group.getId());
//                logger.info("User {} created and added to group {}", customer.getUsername(), groupName);
            } else {
                List<String> details = List.of(
                        "Status: " + response.getStatus(),
                        "Response: " + responseBody,
                        "Reason: " + response.getStatusInfo()
                );
                logger.error("Failed to create user {} in Keycloak. Status: {}, Response: {}, Reason {}",
                        customer.getUsername(), response.getStatus(), responseBody, response.getStatusInfo());
                throw new KeycloakException("Failed to create user in Keycloak, status: " + response.getStatus() + ", reason: " + response.getStatusInfo(), details);
            }
        } catch (Exception e) {
            logger.error("An error occurred while trying to create user in Keycloak: {}", e.getMessage(), e);
            throw new KeycloakException("An error occurred while creating user: " + e.getMessage(), Collections.singletonList(e.getMessage()));
        }
    }

}
