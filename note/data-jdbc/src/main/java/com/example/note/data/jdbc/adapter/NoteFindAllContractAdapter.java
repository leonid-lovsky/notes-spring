package com.example.note.data.jdbc.adapter;

import java.util.List;
import java.util.Map;

import com.example.note.contract.NoteFindAllContract;
import com.example.note.data.jdbc.mapper.NoteRowMapperContract;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllContractAdapter implements NoteFindAllContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final NoteRowMapperContract noteRowMapper;

    NoteFindAllContractAdapter(NamedParameterJdbcTemplate jdbc, NoteRowMapperContract noteRowMapper) {
        this.jdbc = jdbc;
        this.noteRowMapper = noteRowMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.jdbc.query("SELECT id, content FROM notes", Map.of(), this.noteRowMapper::fromRow);
    }

}
