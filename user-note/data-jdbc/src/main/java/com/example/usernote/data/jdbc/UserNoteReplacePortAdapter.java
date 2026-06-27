package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteReplacePort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
class UserNoteReplacePortAdapter implements UserNoteReplacePort {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteReplacePortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void replace(UserNote userNote) {
        jdbc.update("UPDATE user_notes SET role = :role WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userNote.userId(), "noteId", userNote.noteId(), "role", userNote.role().name()));
    }
}
