package com.example.note.data.jdbc.adapter;

import java.util.List;
import java.util.Map;

import com.example.note.contract.NoteFindAllContract;
import com.example.note.data.jdbc.mapper.NoteJdbcMapperContract;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllJdbcAdapter implements NoteFindAllContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final NoteJdbcMapperContract noteJdbcMapper;

    NoteFindAllJdbcAdapter(NamedParameterJdbcTemplate jdbc, NoteJdbcMapperContract noteJdbcMapper) {
        this.jdbc = jdbc;
        this.noteJdbcMapper = noteJdbcMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.jdbc.query("SELECT id, content FROM notes", Map.of(), this.noteJdbcMapper::fromRow);
    }

}
