package com.example.user.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.user.contract.UserRemoveContract;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserRemoveJdbcAdapter implements UserRemoveContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserRemoveJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void remove(UUID id) {
        this.jdbc.update("DELETE FROM users WHERE id = :id", Map.of("id", id));
    }

}
