package com.example.user.data.jdbc;

import com.example.user.domain.UserFindAllPort;
import com.example.user.domain.UserResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
        return jdbc.query("SELECT id, username, email FROM users", Map.of(), userJdbcMapper::fromRow);
    }
}
