package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;

import com.example.user.contract.UserFindByEmailContract;
import com.example.user.data.jdbc.mapper.UserJdbcMapperContract;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailJdbcAdapter implements UserFindByEmailContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserJdbcMapperContract userJdbcMapper;

    UserFindByEmailJdbcAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapperContract userJdbcMapper) {
        this.jdbc = jdbc;
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public Optional<UserResponse> findByEmail(String email) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE email = :email", Map.of("email", email),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

}
