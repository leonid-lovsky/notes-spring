package com.example.usernote.data.jdbc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.usernote.domain.UserNoteFindByNoteIdPort;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdPortAdapter implements UserNoteFindByNoteIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteJdbcMapper userNoteJdbcMapper;

    UserNoteFindByNoteIdPortAdapter(NamedParameterJdbcTemplate jdbc, UserNoteJdbcMapper userNoteJdbcMapper) {
        this.jdbc = jdbc;
        this.userNoteJdbcMapper = userNoteJdbcMapper;
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.jdbc.query("SELECT user_id, note_id, role FROM user_notes WHERE note_id = :noteId",
                Map.of("noteId", noteId), this.userNoteJdbcMapper::fromRow);
    }

}
