package com.example.user.data.jdbc;

import java.util.Map;
import java.util.Optional;

import com.example.user.domain.UserFindByEmailPort;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailPortAdapter implements UserFindByEmailPort {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserJdbcMapper userJdbcMapper;

    UserFindByEmailPortAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapper userJdbcMapper) {
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
