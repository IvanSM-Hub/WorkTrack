package com.worktrack.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worktrack.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByAuthUserId(UUID authUserId);
    Optional<User> findByEmail(String email);

}
