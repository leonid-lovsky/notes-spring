package com.example.user.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.user.contract.UserContract;
import com.example.user.data.jdbc.mapper.UserJdbcMapperContract;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserJdbcAdapter implements UserContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserJdbcMapperContract userJdbcMapper;

    UserJdbcAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapperContract userJdbcMapper) {
        this.jdbc = jdbc;
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO users (id, username, email) VALUES (:id, :username, :email)",
                Map.of("id", id, "username", request.username(), "email", request.email()));
        return new UserResponse(id, request.username(), request.email());
    }

    @Override
    public boolean existsById(UUID id) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE id = :id", Map.of("id", id),
                Integer.class);
        return count != null && count > 0;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.jdbc.query("SELECT id, username, email FROM users", Map.of(), this.userJdbcMapper::fromRow);
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE email = :email", Map.of("email", email),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE id = :id", Map.of("id", id),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE username = :username", Map.of("username", username),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

    @Override
    public void remove(UUID id) {
        this.jdbc.update("DELETE FROM users WHERE id = :id", Map.of("id", id));
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        this.jdbc.update("UPDATE users SET username = :username, email = :email WHERE id = :id",
                Map.of("id", id, "username", request.username(), "email", request.email()));
        return new UserResponse(id, request.username(), request.email());
    }

}
