package com.example.usernote.data.jdbc.adapter;

import java.util.Map;

import com.example.usernote.contract.UserNoteAddContract;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddContractAdapter implements UserNoteAddContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteAddContractAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        this.jdbc.update("INSERT INTO user_notes (user_id, note_id, role) VALUES (:userId, :noteId, :role)",
                Map.of("userId", request.userId(), "noteId", request.noteId(), "role", request.role().name()));
        return new UserNoteResponse(request.userId(), request.noteId(), request.role());
    }

}
