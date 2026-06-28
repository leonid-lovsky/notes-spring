package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;

import com.example.user.contract.UserFindByUsernameContract;
import com.example.user.data.jdbc.mapper.UserRowMapperContract;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernameContractAdapter implements UserFindByUsernameContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserRowMapperContract userRowMapper;

    UserFindByUsernameContractAdapter(NamedParameterJdbcTemplate jdbc, UserRowMapperContract userRowMapper) {
        this.jdbc = jdbc;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<UserResponse> findByUsername(String username) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE username = :username", Map.of("username", username),
                    this.userRowMapper::fromRow)
            .stream()
            .findFirst();
    }

}
