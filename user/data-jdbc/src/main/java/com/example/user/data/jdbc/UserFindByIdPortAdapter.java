package com.example.user.data.jdbc;

import com.example.user.domain.UserFindByIdPort;
import com.example.user.domain.UserResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        return jdbc
            .query("SELECT id, username, email FROM users WHERE id = :id", Map.of("id", id), userJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

}
