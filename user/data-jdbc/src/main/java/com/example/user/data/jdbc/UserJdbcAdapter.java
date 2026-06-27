package com.example.user.data.jdbc;

import com.example.user.domain.User;
import com.example.user.domain.UserRepository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
class UserJdbcAdapter implements UserRepository {

    private final NamedParameterJdbcTemplate jdbc;

    UserJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = :id";
        Integer count = jdbc.queryForObject(sql, Map.of("id", id), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Optional<User> findById(UUID id) {
        String sql = "SELECT id, username, email FROM users WHERE id = :id";
        List<User> results = jdbc.query(sql, Map.of("id", id), UserJdbcAdapter::toUser);
        return results.stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT id, username, email FROM users WHERE username = :username";
        List<User> results = jdbc.query(sql, Map.of("username", username), UserJdbcAdapter::toUser);
        return results.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, username, email FROM users WHERE email = :email";
        List<User> results = jdbc.query(sql, Map.of("email", email), UserJdbcAdapter::toUser);
        return results.stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbc.query("SELECT id, username, email FROM users", UserJdbcAdapter::toUser);
    }

    @Override
    public User add(User user) {
        UUID id = UUID.randomUUID();
        String sql = "INSERT INTO users (id, username, email) VALUES (:id, :username, :email)";
        jdbc.update(sql, Map.of("id", id, "username", user.username(), "email", user.email()));
        return new User(id, user.username(), user.email());
    }

    @Override
    public void replace(User user) {
        String sql = "UPDATE users SET username = :username, email = :email WHERE id = :id";
        jdbc.update(sql, Map.of("id", user.id(), "username", user.username(), "email", user.email()));
    }

    @Override
    public void remove(UUID id) {
        jdbc.update("DELETE FROM users WHERE id = :id", Map.of("id", id));
    }

    private static User toUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getObject("id", UUID.class),
                rs.getString("username"),
                rs.getString("email")
        );
    }
}
