package com.user_management_service.service;

import com.user_management_service.dto.AdminDto;
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
    private final KeycloakService keycloakService;
    private final UserService userService;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(String id) {
        return adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
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
            keycloakUserId = keycloakService.addUser(admin);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create admin in Keycloak", e);
        }
        if (keycloakUserId == null || keycloakUserId.isEmpty()) {
            throw new RuntimeException("Failed to retrieve user ID from Keycloak");
        }
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        admin.setId(keycloakUserId);

            System.out.println(admin.getUsername() + admin.getEmail() + admin.getPhoneNumber());
            var savedAdmin= adminRepository.save(admin);
            return (AdminDto) adminMapper.toDto(savedAdmin);
    }


    public AdminDto updateAdmin(String id, AdminDto adminDto) {
        var existingAdmin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        var updatedAdmin = (Admin) adminMapper.partialUpdate(adminDto, existingAdmin);
        try {
            keycloakService.updateUser(id, adminDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update admin in Keycloak", e);
        }
        try {
            updatedAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            var savedAdmin = adminRepository.save(updatedAdmin);
            return (AdminDto) adminMapper.toDto(savedAdmin);
        } catch (Exception e) {
            keycloakService.deleteUser(id);
            throw new RuntimeException("Failed to update admin in local repository", e);
        }
    }

    public void deleteAdmin(String id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        try {
            keycloakService.deleteUser(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete admin in keycloak", e);
        }
        try {
            adminRepository.delete(admin);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete admin in local repository", e);
        }
    }
}
