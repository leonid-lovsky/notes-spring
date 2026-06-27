package com.example.note.data.jdbc;

import com.example.note.domain.Note;
import com.example.note.domain.NoteReplacePort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
class NoteReplacePortAdapter implements NoteReplacePort {

    private final NamedParameterJdbcTemplate jdbc;

    NoteReplacePortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void replace(Note note) {
        jdbc.update("UPDATE notes SET content = :content WHERE id = :id",
                Map.of("id", note.id(), "content", note.content()));
    }
}
