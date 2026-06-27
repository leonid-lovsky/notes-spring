package com.example.user.data.jdbc;

import com.example.user.domain.User;
import com.example.user.domain.UserFindByEmailPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserFindByEmailPortAdapter implements UserFindByEmailPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserFindByEmailPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbc.query("SELECT id, username, email FROM users WHERE email = :email",
                Map.of("email", email), UserFindByEmailPortAdapter::toUser).stream().findFirst();
    }

    private static User toUser(ResultSet rs, int row) throws SQLException {
        return new User(rs.getObject("id", UUID.class), rs.getString("username"), rs.getString("email"));
    }
}
