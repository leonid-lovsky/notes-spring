package com.example.user.data.jdbc;

import com.example.user.domain.User;
import com.example.user.domain.UserReplacePort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
class UserReplacePortAdapter implements UserReplacePort {

    private final NamedParameterJdbcTemplate jdbc;

    UserReplacePortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void replace(User user) {
        jdbc.update("UPDATE users SET username = :username, email = :email WHERE id = :id",
                Map.of("id", user.id(), "username", user.username(), "email", user.email()));
    }
}
