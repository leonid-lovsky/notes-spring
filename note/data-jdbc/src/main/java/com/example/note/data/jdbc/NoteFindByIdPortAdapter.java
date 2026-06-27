package com.example.note.data.jdbc;

import com.example.note.domain.NoteFindByIdPort;
import com.example.note.domain.NoteResponse;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class NoteFindByIdPortAdapter implements NoteFindByIdPort {

    private final NamedParameterJdbcTemplate jdbc;
    private final NoteJdbcMapper noteJdbcMapper;

    NoteFindByIdPortAdapter(NamedParameterJdbcTemplate jdbc, NoteJdbcMapper noteJdbcMapper) {
        this.jdbc = jdbc;
        this.noteJdbcMapper = noteJdbcMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return jdbc.query("SELECT id, content FROM notes WHERE id = :id",
                Map.of("id", id), noteJdbcMapper::fromRow).stream().findFirst();
    }
}
