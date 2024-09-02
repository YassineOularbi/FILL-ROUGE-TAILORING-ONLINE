package com.user_management_service.service;

import com.user_management_service.dto.UserDto;
import com.user_management_service.mapper.UserMapper;
import com.user_management_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
//    private final KeycloakService keycloakService;

    public List<UserDto> getAllUsers() {
        var users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDto register(UserDto userDto) {
        System.out.println(userDto.getUsername());
//        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
//            throw new RuntimeException("Username already exist !");
//        }
        var user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        System.out.println(user.getUsername());
//        var savedUser = userRepository.save(user);
//        keycloakService.createUserInKeycloak(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName());
        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        var existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        var updatedUser = userMapper.partialUpdate(userDto, existingUser);
        updatedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        var savedUser = userRepository.save(updatedUser);
        return userMapper.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
