package com.user_management_service.service;

import com.user_management_service.dto.TailorDto;
import com.user_management_service.mapper.TailorMapper;
import com.user_management_service.repository.TailorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TailorService {

    private final TailorRepository tailorRepository;
    private final TailorMapper tailorMapper;
    private final PasswordEncoder passwordEncoder;

    public List<TailorDto> getAllTailors() {
        var tailors = tailorRepository.findAll();
        return tailorMapper.toDtoList(tailors);
    }

    public TailorDto getTailorById(Long id) {
        var tailor = tailorRepository.findById(id).orElseThrow(() -> new RuntimeException("Tailor not found"));
        return tailorMapper.toDto(tailor);
    }

    public TailorDto register(TailorDto tailorDto) {
        var tailor = tailorMapper.toEntity(tailorDto);
        tailor.setPassword(passwordEncoder.encode(tailorDto.getPassword()));
        var savedTailor = tailorRepository.save(tailor);
        return tailorMapper.toDto(savedTailor);
    }

    public TailorDto updateTailor(Long id, TailorDto tailorDto) {
        var existingTailor = tailorRepository.findById(id).orElseThrow(() -> new RuntimeException("Tailor not found"));
        var updatedTailor = tailorMapper.partialUpdate(tailorDto, existingTailor);
        updatedTailor.setPassword(passwordEncoder.encode(tailorDto.getPassword()));
        var savedTailor = tailorRepository.save(updatedTailor);
        return tailorMapper.toDto(updatedTailor);
    }

    public void deleteTailor(Long id) {
        var tailor = tailorRepository.findById(id).orElseThrow(() -> new RuntimeException("Tailor not found"));
        tailorRepository.delete(tailor);
    }
}
