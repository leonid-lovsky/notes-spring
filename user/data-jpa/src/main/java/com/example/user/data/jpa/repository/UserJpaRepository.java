package com.example.user.data.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.user.data.jpa.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

}
