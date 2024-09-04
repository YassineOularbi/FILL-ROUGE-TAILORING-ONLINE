package com.user_management_service.service;

import com.user_management_service.dto.TailorDto;
import com.user_management_service.exception.KeycloakServiceException;
import com.user_management_service.exception.TailorNotFoundException;
import com.user_management_service.exception.TailorRepositoryException;
import com.user_management_service.mapper.UserMapper;
import com.user_management_service.model.Tailor;
import com.user_management_service.repository.TailorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TailorService {

    private final TailorRepository tailorRepository;
    private final UserMapper tailorMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationService keycloakService;

    public List<Tailor> getAllTailors() {
        return tailorRepository.findAll();
    }

    public Tailor getTailorById(String id) {
        return tailorRepository.findById(id).orElseThrow(() -> new TailorNotFoundException(id));
    }

    public TailorDto register(TailorDto tailorDto) {
        try {
            userService.getUserByUsername(tailorDto.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
        var tailor = (Tailor) tailorMapper.toEntity(tailorDto);
        String keycloakUserId;
        try {
            keycloakUserId = keycloakService.addUser(tailor);
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to create tailor in Keycloak", e);
        }
        if (keycloakUserId == null || keycloakUserId.isEmpty()) {
            throw new KeycloakServiceException("Failed to retrieve user ID from Keycloak");
        }
        tailor.setPassword(passwordEncoder.encode(tailorDto.getPassword()));
        tailor.setId(keycloakUserId);
        try {
            var savedTailor= tailorRepository.save(tailor);
            return (TailorDto) tailorMapper.toDto(savedTailor);
        } catch (Exception e) {
            keycloakService.deleteUser(keycloakUserId);
            throw new TailorRepositoryException("Failed to save tailor in local repository", e);
        }
    }

    public TailorDto updateTailor(String id, TailorDto tailorDto) {
        var existingTailor = tailorRepository.findById(id).orElseThrow(() -> new TailorNotFoundException(id));
        var updatedTailor = (Tailor) tailorMapper.partialUpdate(tailorDto, existingTailor);
        try {
            keycloakService.updateUser(id, tailorDto);
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to update tailor in Keycloak", e);
        }
        try {
            updatedTailor.setPassword(passwordEncoder.encode(tailorDto.getPassword()));
            var savedtailor = tailorRepository.save(updatedTailor);
            return (TailorDto) tailorMapper.toDto(savedtailor);
        } catch (Exception e) {
            keycloakService.deleteUser(id);
            throw new TailorRepositoryException("Failed to update tailor in local repository", e);
        }
    }

    public void deleteTailor(String id) {
        var tailor = tailorRepository.findById(id).orElseThrow(() -> new TailorNotFoundException(id));
        try {
            keycloakService.deleteUser(id);
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to delete tailor in keycloak", e);
        }
        try {
            tailorRepository.delete(tailor);
        } catch (Exception e) {
            throw new TailorRepositoryException("Failed to delete tailor in local repository", e);
        }
    }
}
