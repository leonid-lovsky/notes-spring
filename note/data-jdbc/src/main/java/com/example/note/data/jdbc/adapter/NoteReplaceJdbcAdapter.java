package com.example.note.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.note.contract.NoteReplaceContract;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteReplaceJdbcAdapter implements NoteReplaceContract {

    private final NamedParameterJdbcTemplate jdbc;

    NoteReplaceJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        this.jdbc.update("UPDATE notes SET content = :content WHERE id = :id",
                Map.of("id", id, "content", request.content()));
        return new NoteResponse(id, request.content());
    }

}
