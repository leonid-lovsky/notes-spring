package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.user.contract.UserFindByIdContract;
import com.example.user.data.jdbc.mapper.UserRowMapperContract;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdContractAdapter implements UserFindByIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserRowMapperContract userRowMapper;

    UserFindByIdContractAdapter(NamedParameterJdbcTemplate jdbc, UserRowMapperContract userRowMapper) {
        this.jdbc = jdbc;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE id = :id", Map.of("id", id),
                    this.userRowMapper::fromRow)
            .stream()
            .findFirst();
    }

}
