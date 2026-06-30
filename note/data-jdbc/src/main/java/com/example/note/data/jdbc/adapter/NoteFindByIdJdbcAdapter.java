package com.example.note.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteFindByIdContract;
import com.example.note.data.jdbc.mapper.NoteJdbcMapperContract;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdJdbcAdapter implements NoteFindByIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final NoteJdbcMapperContract noteJdbcMapper;

    NoteFindByIdJdbcAdapter(NamedParameterJdbcTemplate jdbc, NoteJdbcMapperContract noteJdbcMapper) {
        this.jdbc = jdbc;
        this.noteJdbcMapper = noteJdbcMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, content FROM notes WHERE id = :id", Map.of("id", id), this.noteJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

}
