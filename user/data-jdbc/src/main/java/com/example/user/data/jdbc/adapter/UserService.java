package com.example.user.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.user.contract.UserServiceInterface;
import com.example.user.data.jdbc.mapper.UserJdbcMapperContract;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserService implements UserServiceInterface {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserJdbcMapperContract userJdbcMapper;

    UserService(NamedParameterJdbcTemplate jdbc, UserJdbcMapperContract userJdbcMapper) {
        this.jdbc = jdbc;
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public Boolean existsById(UUID id) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE id = :id", Map.of("id", id),
                Integer.class);
        return count != null && count > 0;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO users (id, username, email) VALUES (:id, :username, :email)",
                Map.of("id", id, "username", request.username(), "email", request.email()));
        return new UserResponse(id, request.username(), request.email());
    }

    @Override
    public List<UserResponse> findAll() {
        return this.jdbc.query("SELECT id, username, email FROM users", Map.of(), this.userJdbcMapper::fromRow);
    }

    @Override
    public UserResponse findById(UUID id) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE id = :id", Map.of("id", id),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst()
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserResponse findByEmail(String email) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE email = :email", Map.of("email", email),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst()
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public UserResponse findByUsername(String username) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE username = :username", Map.of("username", username),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst()
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        findById(id);
        this.jdbc.update("UPDATE users SET username = :username, email = :email WHERE id = :id",
                Map.of("id", id, "username", request.username(), "email", request.email()));
        return new UserResponse(id, request.username(), request.email());
    }

    @Override
    public UserResponse merge(UUID id, UserRequest request) {
        UserResponse existing = findById(id);
        UserRequest merged = merge(existing, request);
        this.jdbc.update("UPDATE users SET username = :username, email = :email WHERE id = :id",
                Map.of("id", id, "username", merged.username(), "email", merged.email()));
        return new UserResponse(id, merged.username(), merged.email());
    }

    @Override
    public UserResponse remove(UUID id) {
        UserResponse existing = findById(id);
        this.jdbc.update("DELETE FROM users WHERE id = :id", Map.of("id", id));
        return existing;
    }

    private static UserRequest merge(UserResponse existing, UserRequest request) {
        String username = (request.username() != null) ? request.username() : existing.username();
        String email = (request.email() != null) ? request.email() : existing.email();
        return new UserRequest(username, email);
    }

}
