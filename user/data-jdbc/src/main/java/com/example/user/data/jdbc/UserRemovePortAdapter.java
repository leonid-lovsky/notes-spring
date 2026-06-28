package com.example.user.data.jdbc;

import java.util.Map;
import java.util.UUID;

import com.example.user.domain.UserRemovePort;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserRemovePortAdapter implements UserRemovePort {

    private final NamedParameterJdbcTemplate jdbc;

    UserRemovePortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void remove(UUID id) {
        this.jdbc.update("DELETE FROM users WHERE id = :id", Map.of("id", id));
    }

}
