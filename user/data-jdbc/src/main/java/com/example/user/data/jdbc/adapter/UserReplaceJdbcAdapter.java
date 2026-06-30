package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.user.contract.UserReplaceContract;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserReplaceJdbcAdapter implements UserReplaceContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserReplaceJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserResponse replace(UUID id, UserRequest request) {
        this.jdbc.update("UPDATE users SET username = :username, email = :email WHERE id = :id",
                Map.of("id", id, "username", request.username(), "email", request.email()));
        return new UserResponse(id, request.username(), request.email());
    }

}
