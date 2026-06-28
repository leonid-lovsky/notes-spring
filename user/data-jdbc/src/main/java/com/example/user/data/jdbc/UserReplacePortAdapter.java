package com.example.user.data.jdbc;

import java.util.Map;
import java.util.UUID;

import com.example.user.domain.UserReplacePort;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserReplacePortAdapter implements UserReplacePort {

    private final NamedParameterJdbcTemplate jdbc;

    UserReplacePortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        this.jdbc.update("UPDATE users SET username = :username, email = :email WHERE id = :id",
                Map.of("id", id, "username", request.username(), "email", request.email()));
        return new UserResponse(id, request.username(), request.email());
    }

}
