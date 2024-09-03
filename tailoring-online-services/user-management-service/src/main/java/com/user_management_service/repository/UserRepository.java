package com.user_management_service.repository;

import com.user_management_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "select u from User u where u.username =?1", nativeQuery = true)
    Optional<User> findByUsername(String username);
}
