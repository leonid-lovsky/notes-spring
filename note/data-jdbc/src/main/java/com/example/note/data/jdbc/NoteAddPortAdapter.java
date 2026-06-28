package com.example.note.data.jdbc;

import java.util.Map;
import java.util.UUID;

import com.example.note.domain.NoteAddPort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteAddPortAdapter implements NoteAddPort {

    private final NamedParameterJdbcTemplate jdbc;

    NoteAddPortAdapter(NamedParameterJdbcTemplate jdbc) {
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
