package com.example.usernote.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceContractAdapter implements UserNoteReplaceContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteReplaceContractAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        this.jdbc.update("UPDATE user_notes SET role = :role WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId, "role", request.role().name()));
        return new UserNoteResponse(userId, noteId, request.role());
    }

}
