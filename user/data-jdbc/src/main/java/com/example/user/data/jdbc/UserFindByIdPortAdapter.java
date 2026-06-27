package com.example.user.data.jdbc;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByIdPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserFindByIdPortAdapter implements UserFindByIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserFindByIdPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jdbc.query("SELECT id, username, email FROM users WHERE id = :id",
                Map.of("id", id), UserFindByIdPortAdapter::toUser).stream().findFirst();
    }

    private static User toUser(ResultSet rs, int row) throws SQLException {
        return new User(rs.getObject("id", UUID.class), rs.getString("username"), rs.getString("email"));
    }
}
