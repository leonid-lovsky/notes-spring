package com.example.user.data.jdbc.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.user.data.jdbc.model.UserJdbcEntity;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJdbcRepository extends ListCrudRepository<UserJdbcEntity, UUID> {

    Optional<UserJdbcEntity> findByUsername(String username);

    Optional<UserJdbcEntity> findByEmail(String email);
}
