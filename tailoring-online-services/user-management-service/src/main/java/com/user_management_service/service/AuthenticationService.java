package com.user_management_service.service;

import com.user_management_service.config.KeycloakConfig;
import com.user_management_service.dto.CreateCustomerDto;
import com.user_management_service.enums.*;
import com.user_management_service.mapper.CustomerMapper;
import com.user_management_service.validation.CreateGroup;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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

    @Value("${keycloak.resource}")
    private String clientId;

    public void registerCustomer(@Validated(CreateGroup.class) CreateCustomerDto createCustomerDto) {
        var customer = customerMapper.toCreateEntity(createCustomerDto);

        logger.info("Starting to add user: {}", customer.getUsername());

        Keycloak keycloak = keycloakConfig.getInstance(username, password);
        UsersResource usersResource = keycloak.realm(realm).users();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(customer.getUsername());
        userRepresentation.setFirstName(customer.getFirstName());
        userRepresentation.setLastName(customer.getLastName());
        userRepresentation.setEmail(customer.getEmail());
        userRepresentation.setEnabled(false);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setTotp(false);

        userRepresentation.setCredentials(Collections.singletonList(keycloakConfig.createPasswordCredentials(customer.getPassword())));

        List<RoleRepresentation> realmRoles = keycloak.realm(realm).roles().list();
        RoleRepresentation customerRole = realmRoles.stream()
                .filter(role -> Role.CUSTOMER.name().equals(role.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role 'CUSTOMER' not found"));

        userRepresentation.setRealmRoles(Collections.singletonList(customerRole.getName()));
        userRepresentation.setGroups(Collections.singletonList(Role.CUSTOMER.name()));

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phoneNumber", Collections.singletonList(customer.getPhoneNumber()));
        attributes.put("dateOfBirth", Collections.singletonList(customer.getDateOfBirth().toString()));
        attributes.put("gender", Collections.singletonList(customer.getGender().name()));
        attributes.put("lastLogin", Collections.singletonList("null"));
        attributes.put("languagePreference", Collections.singletonList(customer.getLanguagePreference().name()));
        attributes.put("status", Collections.singletonList(customer.getStatus().name()));
        attributes.put("profilePicture", Collections.singletonList(customer.getProfilePicture()));
        attributes.put("loyaltyPoints", Collections.singletonList(customer.getLoyaltyPoints().toString()));
        userRepresentation.setAttributes(attributes);

        logger.info("Attempting to create user in Keycloak: {}", userRepresentation);
        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            logger.info("User {} created successfully in Keycloak", customer.getUsername());
        } else {
            logger.error("Failed to create user {} in Keycloak. Status: {}", customer.getUsername(), response.getStatus());
        }
    }
}
