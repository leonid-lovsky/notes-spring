package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByUserIdAndNoteIdPort;
import com.example.usernote.domain.UserNoteRole;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdAndNoteIdPortAdapter implements UserNoteFindByUserIdAndNoteIdPort {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteFindByUserIdAndNoteIdPortAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return jdbc.query(
                "SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId),
                UserNoteFindByUserIdAndNoteIdPortAdapter::toUserNote).stream().findFirst();
    }

    private static UserNote toUserNote(ResultSet rs, int row) throws SQLException {
        return new UserNote(rs.getObject("user_id", UUID.class),
                rs.getObject("note_id", UUID.class), UserNoteRole.valueOf(rs.getString("role")));
    }
}
