package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByNoteIdPort;
import com.example.usernote.domain.UserNoteRole;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
class UserNoteFindByNoteIdPortAdapter implements UserNoteFindByNoteIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteFindByNoteIdPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<UserNote> findByNoteId(UUID noteId) {
        return jdbc.query("SELECT user_id, note_id, role FROM user_notes WHERE note_id = :noteId",
                Map.of("noteId", noteId), UserNoteFindByNoteIdPortAdapter::toUserNote);
    }

    private static UserNote toUserNote(ResultSet rs, int row) throws SQLException {
        return new UserNote(rs.getObject("user_id", UUID.class),
                rs.getObject("note_id", UUID.class), UserNoteRole.valueOf(rs.getString("role")));
    }
}
