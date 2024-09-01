package com.user_management_service.service;

import com.user_management_service.dto.AdminDto;
import com.user_management_service.mapper.AdminMapper;
import com.user_management_service.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    public List<AdminDto> getAllAdmins() {
        var admins = adminRepository.findAll();
        return adminMapper.toDtoList(admins);
    }

    public AdminDto getAdminById(Long id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        return adminMapper.toDto(admin);
    }

    public AdminDto register(AdminDto adminDto) {
        var admin = adminMapper.toEntity(adminDto);
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        var savedAdmin = adminRepository.save(admin);
        return adminMapper.toDto(savedAdmin);
    }

    public AdminDto updateAdmin(Long id, AdminDto adminDto) {
        var existingAdmin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        var updatedAdmin = adminMapper.partialUpdate(adminDto, existingAdmin);
        updatedAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        var savedAdmin = adminRepository.save(updatedAdmin);
        return adminMapper.toDto(savedAdmin);
    }

    public void deleteAdmin(Long id) {
        var admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        adminRepository.delete(admin);
    }
}
