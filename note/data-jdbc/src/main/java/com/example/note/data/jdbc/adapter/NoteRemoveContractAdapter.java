package com.example.note.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.note.contract.NoteRemoveContract;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteRemoveContractAdapter implements NoteRemoveContract {

    private final NamedParameterJdbcTemplate jdbc;

    NoteRemoveContractAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void remove(UUID id) {
        this.jdbc.update("DELETE FROM notes WHERE id = :id", Map.of("id", id));
    }

}
