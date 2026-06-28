package com.example.note.data.jdbc.adapter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteFindByIdContract;
import com.example.note.data.jdbc.mapper.NoteRowMapperContract;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdContractAdapter implements NoteFindByIdContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final NoteRowMapperContract noteRowMapper;

    NoteFindByIdContractAdapter(NamedParameterJdbcTemplate jdbc, NoteRowMapperContract noteRowMapper) {
        this.jdbc = jdbc;
        this.noteRowMapper = noteRowMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, content FROM notes WHERE id = :id", Map.of("id", id), this.noteRowMapper::fromRow)
            .stream()
            .findFirst();
    }

}
