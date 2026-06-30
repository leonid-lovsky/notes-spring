package com.example.note.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.note.contract.NoteAddContract;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteAddJdbcAdapter implements NoteAddContract {

    private final NamedParameterJdbcTemplate jdbc;

    NoteAddJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO notes (id, content) VALUES (:id, :content)",
                Map.of("id", id, "content", request.content()));
        return new NoteResponse(id, request.content());
    }

}
