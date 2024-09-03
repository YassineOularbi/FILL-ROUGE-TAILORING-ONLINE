package com.user_management_service.service;

import com.user_management_service.model.User;
import com.user_management_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void getUserByUsername(String username){
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameNotFoundException("Username already exists!");
        }
        if (!keycloakService.getUser(username).isEmpty()) {
            throw new UsernameNotFoundException("Username already exists!");
        }
    }
}