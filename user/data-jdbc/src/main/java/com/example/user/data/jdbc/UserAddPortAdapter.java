package com.example.user.data.jdbc;

import com.example.user.domain.User;
import com.example.user.domain.UserAddPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class UserAddPortAdapter implements UserAddPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserAddPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User add(User user) {
        UUID id = UUID.randomUUID();
        jdbc.update("INSERT INTO users (id, username, email) VALUES (:id, :username, :email)",
                Map.of("id", id, "username", user.username(), "email", user.email()));
        return new User(id, user.username(), user.email());
    }
}
