package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;

import com.example.user.contract.UserFindByEmailContract;
import com.example.user.data.jdbc.mapper.UserRowMapperContract;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailContractAdapter implements UserFindByEmailContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserRowMapperContract userRowMapper;

    UserFindByEmailContractAdapter(NamedParameterJdbcTemplate jdbc, UserRowMapperContract userRowMapper) {
        this.jdbc = jdbc;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE email = :email", Map.of("email", email),
                    this.userRowMapper::fromRow)
            .stream()
            .findFirst();
    }

}
