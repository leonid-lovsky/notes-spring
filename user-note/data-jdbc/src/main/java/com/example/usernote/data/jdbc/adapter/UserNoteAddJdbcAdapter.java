package com.example.usernote.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteAddContract;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddJdbcAdapter implements UserNoteAddContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteAddJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO user_notes (id, user_id, note_id, role) VALUES (:id, :userId, :noteId, :role)",
                Map.of("id", id, "userId", request.userId(), "noteId", request.noteId(), "role",
                        request.role().name()));
        return new UserNoteResponse(id, request.userId(), request.noteId(), request.role());
    }

}
