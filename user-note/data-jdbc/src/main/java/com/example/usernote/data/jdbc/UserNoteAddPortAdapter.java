package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteAddPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
class UserNoteAddPortAdapter implements UserNoteAddPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteAddPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public UserNote add(UserNote userNote) {
        jdbc.update("INSERT INTO user_notes (user_id, note_id, role) VALUES (:userId, :noteId, :role)",
                Map.of("userId", userNote.userId(), "noteId", userNote.noteId(), "role", userNote.role().name()));
        return userNote;
    }
}
