package com.example.note.data.jdbc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteAddPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class NoteAddPortAdapter implements NoteAddPort {

    private final NamedParameterJdbcTemplate jdbc;

    NoteAddPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Note add(Note note) {
        UUID id = UUID.randomUUID();
        jdbc.update("INSERT INTO notes (id, content) VALUES (:id, :content)",
                Map.of("id", id, "content", note.content()));
        return new Note(id, note.content());
    }
}
