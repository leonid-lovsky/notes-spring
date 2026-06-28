package com.example.note.data.jdbc;

import java.util.Map;
import java.util.UUID;

import com.example.note.domain.NoteRemovePort;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteRemovePortAdapter implements NoteRemovePort {

    private final NamedParameterJdbcTemplate jdbc;

    NoteRemovePortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void remove(UUID id) {
        this.jdbc.update("DELETE FROM notes WHERE id = :id", Map.of("id", id));
    }

}
