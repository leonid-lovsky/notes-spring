package com.example.user.data.jdbc;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.user.domain.UserFindByIdPort;
import com.example.user.domain.UserResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdPortAdapter implements UserFindByIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserJdbcMapper userJdbcMapper;

    UserFindByIdPortAdapter(NamedParameterJdbcTemplate jdbc, UserJdbcMapper userJdbcMapper) {
        this.jdbc = jdbc;
        this.userJdbcMapper = userJdbcMapper;
    }

    @Override
    public Optional<UserResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, username, email FROM users WHERE id = :id", Map.of("id", id),
                    this.userJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

}
