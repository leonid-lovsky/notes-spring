package com.example.user.data.jdbc;

import java.util.List;
import java.util.Map;

import com.example.user.domain.UserFindAllPort;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserJdbcMapper userJdbcMapper;

    UserFindAllPortAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapper userJdbcMapper) {
        this.jdbc = jdbc;
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public List<UserResponse> findAll() {
        return this.jdbc.query("SELECT id, username, email FROM users", Map.of(), this.userJdbcMapper::fromRow);
    }

}
