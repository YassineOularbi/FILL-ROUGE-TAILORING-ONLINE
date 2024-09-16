package com.user_management_service.service;

import com.user_management_service.dto.AdminDto;
import com.user_management_service.exception.AdminNotFoundException;
import com.user_management_service.exception.AdminRepositoryException;
import com.user_management_service.exception.KeycloakServiceException;
import com.user_management_service.mapper.UserMapper;
import com.user_management_service.model.Admin;
import com.user_management_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(String id) {
        return adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
    }

    public AdminDto register(AdminDto adminDto) {
        try {
            userService.getUserByUsername(adminDto.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

        var admin = (Admin) adminMapper.toEntity(adminDto);
        String keycloakUserId;
        try {
            keycloakUserId = authenticationService.addUser(admin);
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to create admin in Keycloak", e);
        }
        if (keycloakUserId == null || keycloakUserId.isEmpty()) {
            throw new KeycloakServiceException("Failed to retrieve user ID from Keycloak");
        }
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setId(keycloakUserId);
        try {
            var savedAdmin = adminRepository.save(admin);
            return (AdminDto) adminMapper.toDto(savedAdmin);
        } catch (Exception e) {
            authenticationService.deleteUser(keycloakUserId);
            throw new AdminRepositoryException("Failed to save admin in local repository", e);
        }
    }



    public AdminDto updateAdmin(String id, AdminDto adminDto) {
        var existingAdmin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        var updatedAdmin = (Admin) adminMapper.partialUpdate(adminDto, existingAdmin);
        try {
            authenticationService.updateUser(id, adminDto);
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to update admin in Keycloak", e);
        }
        try {
            updatedAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            var savedAdmin = adminRepository.save(updatedAdmin);
            return (AdminDto) adminMapper.toDto(savedAdmin);
        } catch (Exception e) {
            authenticationService.deleteUser(id);
            throw new AdminRepositoryException("Failed to update admin in local repository", e);
        }
    }

    public void deleteAdmin(String id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new AdminNotFoundException(id));
        try {
            authenticationService.deleteUser(id);
        } catch (Exception e) {
            throw new KeycloakServiceException("Failed to delete admin in Keycloak", e);
        }
        try {
            adminRepository.delete(admin);
        } catch (Exception e) {
            throw new AdminRepositoryException("Failed to delete admin in local repository", e);
        }
    }
}
