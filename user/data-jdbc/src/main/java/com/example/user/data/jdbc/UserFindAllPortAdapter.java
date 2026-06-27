package com.example.user.data.jdbc;

import com.example.user.domain.User;
import com.example.user.domain.UserFindAllPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
class UserFindAllPortAdapter implements UserFindAllPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserFindAllPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<User> findAll() {
        return jdbc.query("SELECT id, username, email FROM users", Map.of(), UserFindAllPortAdapter::toUser);
    }

    private static User toUser(ResultSet rs, int row) throws SQLException {
        return new User(rs.getObject("id", UUID.class), rs.getString("username"), rs.getString("email"));
    }
}
