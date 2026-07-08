package com.example.usernote.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteContract;
import com.example.usernote.data.jdbc.mapper.UserNoteJdbcMapperContract;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteJdbcAdapter implements UserNoteContract {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteJdbcMapperContract userNoteJdbcMapper;

    UserNoteJdbcAdapter(NamedParameterJdbcTemplate jdbc, UserNoteJdbcMapperContract userNoteJdbcMapper) {
        this.jdbc = jdbc;
        this.userNoteJdbcMapper = userNoteJdbcMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO user_notes (id, user_id, note_id, role) VALUES (:id, :userId, :noteId, :role)",
                Map.of("id", id, "userId", request.userId(), "noteId", request.noteId(), "role",
                        request.role().name()));
        return new UserNoteResponse(id, request.userId(), request.noteId(), request.role());
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        Integer count = this.jdbc.queryForObject(
                "SELECT COUNT(*) FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Optional<UserNoteResponse> findById(UUID id) {
        return this.jdbc
            .query("SELECT id, user_id, note_id, role FROM user_notes WHERE id = :id", Map.of("id", id),
                    this.userNoteJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.jdbc.query("SELECT id, user_id, note_id, role FROM user_notes WHERE note_id = :noteId",
                Map.of("noteId", noteId), this.userNoteJdbcMapper::fromRow);
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.jdbc
            .query("SELECT id, user_id, note_id, role FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                    Map.of("userId", userId, "noteId", noteId), this.userNoteJdbcMapper::fromRow)
            .stream()
            .findFirst();
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.jdbc.query("SELECT id, user_id, note_id, role FROM user_notes WHERE user_id = :userId",
                Map.of("userId", userId), this.userNoteJdbcMapper::fromRow);
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.jdbc.update("DELETE FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId));
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
