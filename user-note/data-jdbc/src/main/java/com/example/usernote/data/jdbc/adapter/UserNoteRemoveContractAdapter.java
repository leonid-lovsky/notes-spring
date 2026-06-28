package com.example.usernote.data.jdbc.adapter;

import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteRemoveContract;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemoveContractAdapter implements UserNoteRemoveContract {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteRemoveContractAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.jdbc.update("DELETE FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId));
    }

}
