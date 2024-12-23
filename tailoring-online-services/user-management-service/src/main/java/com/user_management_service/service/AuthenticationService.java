package com.user_management_service.service;

import com.user_management_service.config.KeycloakConfig;
import com.user_management_service.dto.*;
import com.user_management_service.exception.*;
import com.user_management_service.mapper.CustomerMapper;
import com.user_management_service.messaging.*;
import com.user_management_service.validation.CreateGroup;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.*;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final KeycloakConfig keycloakConfig;
    private final CustomerMapper customerMapper;
    private final KafkaProducer kafkaProducer;
    private final KafkaConsumer kafkaConsumer;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AccessTokenResponse login(AuthenticationRequest authenticationRequest) {
        try {
            var keycloak = keycloakConfig.getAuthenticationInstance(authenticationRequest.username(), authenticationRequest.password());
            return keycloak.tokenManager().getAccessToken();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid credentials");
        }
    }

    public void registerCustomer(@Validated(CreateGroup.class) CreateCustomerDto createCustomerDto, MultipartFile profilePicture) throws IOException, UserAlreadyExistsException, KeycloakException {
        var customer = customerMapper.toCreateEntity(createCustomerDto);
        passwordValidator(customer.getPassword(), customer.getUsername(), customer.getEmail());
        logger.info("Starting to add user: {}", customer.getUsername());
        UsersResource usersResource = keycloakConfig.getRealmResource().users();
        if (!createCustomerDto.createUserDto().password().equals(createCustomerDto.createUserDto().confirmPassword())) {
            List<String> errors = new ArrayList<>();
            errors.add("Password confirmation invalid !");
            errors.add("Password doesn't match confirm password invalid !");
            throw new RegistrationException("Password Invalid", errors);
        }
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
        userRepresentation.setUsername(customer.getUsername().toLowerCase());
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
                if (profilePicture != null && !profilePicture.isEmpty()) {
                    try {
                        String pictureId = UUID.randomUUID().toString();
                        kafkaProducer.sendProfilePicture(profilePicture, pictureId);
                        logger.info("Profile picture sent successfully with ID {} for user {}", pictureId, customer.getUsername());
                        long startTime = System.currentTimeMillis();
                        long maxWaitTimeMs = 10000;
                        long pollIntervalMs = 500;
                        String profilePictureUrl = null;
                        try {
                            while ((System.currentTimeMillis() - startTime) < maxWaitTimeMs) {
                                profilePictureUrl = kafkaConsumer.getProfilePictureUrl(pictureId);
                                if (profilePictureUrl != null) {
                                    break;
                                }
                                Thread.sleep(pollIntervalMs);
                            }
                        } catch (InterruptedException e) {
                            logger.error("Thread interruption while waiting for profile picture URL: {}", e.getMessage());
                            throw new RegistrationException(e.getMessage(), Collections.singletonList("Thread interruption while waiting for profile picture URL for user " + customer.getUsername()));
                        } catch (Exception e) {
                            logger.error("Error while consuming profile picture URL: {}", e.getMessage());
                            throw new RegistrationException(e.getMessage(), Collections.singletonList("Error while consuming profile picture URL for user " + customer.getUsername()));
                        }
                        if (profilePictureUrl != null) {
                            customer.setProfilePicture(profilePictureUrl);
                            UserRepresentation updatedUser = usersResource.search(customer.getUsername()).get(0);
                            Map<String, List<String>> updatedAttributes = updatedUser.getAttributes();
                            updatedAttributes.put("profilePicture", Collections.singletonList(profilePictureUrl));
                            updatedUser.setAttributes(updatedAttributes);
                            usersResource.get(updatedUser.getId()).update(updatedUser);
                            logger.info("Profile picture URL retrieved successfully with ID {} for user {}", pictureId, customer.getUsername());
                        } else {
                            throw new RegistrationException("Profile picture URL is null", Collections.singletonList("Profile picture URL is null for ID " + pictureId + " and user " + customer.getUsername()));
                        }
                    } catch (Exception e) {
                        logger.error("Attempt to handle profile picture resulted in an exception: {}", e.getMessage());
                        throw new RegistrationException(e.getMessage(), Collections.singletonList("Failed to handle profile picture for user " + customer.getUsername()));
                    }
                }
            } else {
                List<String> details = List.of(
                        "Status: " + response.getStatus(),
                        "Response: " + responseBody,
                        "Reason: " + response.getStatusInfo()
                );
                logger.error("Failed to create user {} in Keycloak. Status: {}, Response: {}, Reason {}", customer.getUsername(), response.getStatus(), responseBody, response.getStatusInfo());
                throw new KeycloakException("Failed to create user in Keycloak, status: " + response.getStatus() + ", reason: " + response.getStatusInfo(), details);
            }
        } catch (Exception e) {
            logger.error("An error occurred while trying to create user in Keycloak: {}", e.getMessage(), e);
            throw new KeycloakException("An error occurred while creating user: " + e.getMessage(), Collections.singletonList(e.getMessage()));
        }
    }

    private void passwordValidator(String password, String username, String email) {
        List<String> errors = new ArrayList<>();
        if (password.contains(username)) {
            errors.add("Password cannot contain username.");
        }
        if (password.contains(email)) {
            errors.add("Password cannot contain email.");
        }
        if (!errors.isEmpty()) {
            throw new RegistrationException("Invalid input provided", errors);
        }
    }
}
