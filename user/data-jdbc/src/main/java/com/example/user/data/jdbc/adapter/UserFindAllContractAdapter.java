package com.example.user.data.jdbc.adapter;

import java.util.List;
import java.util.Map;

import com.example.user.contract.UserFindAllContract;
import com.example.user.data.jdbc.mapper.UserRowMapperContract;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindAllContractAdapter implements UserFindAllContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserRowMapperContract userRowMapper;

    UserFindAllContractAdapter(NamedParameterJdbcTemplate jdbc, UserRowMapperContract userRowMapper) {
        this.jdbc = jdbc;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.jdbc.query("SELECT id, username, email FROM users", Map.of(), this.userRowMapper::fromRow);
    }

}
