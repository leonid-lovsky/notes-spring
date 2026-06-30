package com.example.usernote.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceJdbcAdapter implements UserNoteReplaceContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteReplaceJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UUID id = this.jdbc
            .query("SELECT id FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                    Map.of("userId", userId, "noteId", noteId), (rs, rowNum) -> rs.getObject("id", UUID.class))
            .stream()
            .findFirst()
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        this.jdbc.update("UPDATE user_notes SET role = :role WHERE id = :id",
                Map.of("id", id, "role", request.role().name()));
        return new UserNoteResponse(id, userId, noteId, request.role());
    }

}
