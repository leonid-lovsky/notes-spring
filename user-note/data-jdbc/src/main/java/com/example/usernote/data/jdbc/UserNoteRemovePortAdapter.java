package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNoteRemovePort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
class UserNoteRemovePortAdapter implements UserNoteRemovePort {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteRemovePortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        jdbc.update("DELETE FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId));
    }
}
