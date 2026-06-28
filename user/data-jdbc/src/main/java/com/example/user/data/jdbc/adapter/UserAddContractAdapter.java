package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.user.contract.UserAddContract;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserAddContractAdapter implements UserAddContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserAddContractAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserResponse add(UserRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO users (id, username, email) VALUES (:id, :username, :email)",
                Map.of("id", id, "username", request.username(), "email", request.email()));
        return new UserResponse(id, request.username(), request.email());
    }

}
