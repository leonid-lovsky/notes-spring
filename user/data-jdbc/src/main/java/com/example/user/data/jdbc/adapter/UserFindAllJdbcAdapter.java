package com.example.user.data.jdbc.adapter;

import java.util.List;
import java.util.Map;

import com.example.user.contract.UserFindAllContract;
import com.example.user.data.jdbc.mapper.UserJdbcMapperContract;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindAllJdbcAdapter implements UserFindAllContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserJdbcMapperContract userJdbcMapper;

    UserFindAllJdbcAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapperContract userJdbcMapper) {
        this.jdbc = jdbc;
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.jdbc.query("SELECT id, username, email FROM users", Map.of(), this.userJdbcMapper::fromRow);
    }

}
