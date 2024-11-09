//package com.user_management_service.service;
//
//import com.user_management_service.exception.UserNotFoundException;
//import com.user_management_service.model.User;
//import com.user_management_service.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final AuthenticationService authenticationService;
//
//
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public User getUserById(String id) {
//        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//    }
//
//    public void getUserByUsername(String username){
//        if (userRepository.findByUsername(username).isPresent()) {
//            throw new BadCredentialsException("Bad credentials !");
//        }
//        if (!authenticationService.getUser(username).isEmpty()) {
//            throw new BadCredentialsException("Bad credentials !");
//        }
//    }
//}
