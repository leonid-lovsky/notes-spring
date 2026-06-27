package com.example.note.data.jdbc;

import com.example.note.domain.NoteFindAllPort;
import com.example.note.domain.NoteResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
class NoteFindAllPortAdapter implements NoteFindAllPort {

    private final NamedParameterJdbcTemplate jdbc;
    private final NoteJdbcMapper noteJdbcMapper;

    NoteFindAllPortAdapter(NamedParameterJdbcTemplate jdbc, NoteJdbcMapper noteJdbcMapper) {
        this.jdbc = jdbc;
        this.noteJdbcMapper = noteJdbcMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return jdbc.query("SELECT id, content FROM notes", Map.of(), noteJdbcMapper::fromRow);
    }
}
