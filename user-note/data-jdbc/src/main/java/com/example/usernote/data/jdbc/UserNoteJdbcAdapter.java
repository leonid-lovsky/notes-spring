package com.example.usernote.data.jdbc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteRepository;
import com.example.usernote.domain.UserNoteRole;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
class UserNoteJdbcAdapter implements UserNoteRepository {

    private final NamedParameterJdbcTemplate jdbc;

    UserNoteJdbcAdapter(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        String sql = "SELECT COUNT(*) FROM user_notes WHERE user_id = :userId AND note_id = :noteId";
        Integer count = jdbc.queryForObject(sql, Map.of("userId", userId, "noteId", noteId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        String sql = "SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId AND note_id = :noteId";
        List<UserNote> results = jdbc.query(sql, Map.of("userId", userId, "noteId", noteId), UserNoteJdbcAdapter::toUserNote);
        return results.stream().findFirst();
    }

    @Override
    public List<UserNote> findByUserId(UUID userId) {
        String sql = "SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId";
        return jdbc.query(sql, Map.of("userId", userId), UserNoteJdbcAdapter::toUserNote);
    }

    @Override
    public List<UserNote> findByNoteId(UUID noteId) {
        String sql = "SELECT user_id, note_id, role FROM user_notes WHERE note_id = :noteId";
        return jdbc.query(sql, Map.of("noteId", noteId), UserNoteJdbcAdapter::toUserNote);
    }

    @Override
    public UserNote add(UserNote userNote) {
        String sql = "INSERT INTO user_notes (user_id, note_id, role) VALUES (:userId, :noteId, :role)";
        jdbc.update(sql, Map.of("userId", userNote.userId(), "noteId", userNote.noteId(), "role", userNote.role().name()));
        return userNote;
    }

    @Override
    public void replace(UserNote userNote) {
        String sql = "UPDATE user_notes SET role = :role WHERE user_id = :userId AND note_id = :noteId";
        jdbc.update(sql, Map.of("userId", userNote.userId(), "noteId", userNote.noteId(), "role", userNote.role().name()));
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        String sql = "DELETE FROM user_notes WHERE user_id = :userId AND note_id = :noteId";
        jdbc.update(sql, Map.of("userId", userId, "noteId", noteId));
    }

    private static UserNote toUserNote(ResultSet rs, int rowNum) throws SQLException {
        return new UserNote(
                rs.getObject("user_id", UUID.class),
                rs.getObject("note_id", UUID.class),
                UserNoteRole.valueOf(rs.getString("role"))
        );
    }
}
