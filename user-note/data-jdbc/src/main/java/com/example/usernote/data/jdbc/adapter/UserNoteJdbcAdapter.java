package com.example.usernote.data.jdbc.adapter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.usernote.contract.UserNoteService;
import com.example.usernote.data.jdbc.mapper.UserNoteJdbcMapperContract;
import com.example.usernote.domain.NoteNotFoundException;
import com.example.usernote.domain.UserNotFoundException;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import com.example.usernote.domain.UserNoteRole;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteJdbcAdapter implements UserNoteService {

    private final NamedParameterJdbcTemplate jdbc;

    private final UserNoteJdbcMapperContract userNoteJdbcMapper;

    UserNoteJdbcAdapter(NamedParameterJdbcTemplate jdbc, UserNoteJdbcMapperContract userNoteJdbcMapper) {
        this.jdbc = jdbc;
        this.userNoteJdbcMapper = userNoteJdbcMapper;
    }

    @Override
    public Boolean existsByUserNoteId(UUID userNoteId) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM user_notes WHERE id = :id",
                Map.of("id", userNoteId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Boolean existsByUserId(UUID userId) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM user_notes WHERE user_id = :userId",
                Map.of("userId", userId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Boolean existsByNoteId(UUID noteId) {
        Integer count = this.jdbc.queryForObject("SELECT COUNT(*) FROM user_notes WHERE note_id = :noteId",
                Map.of("noteId", noteId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        Integer count = this.jdbc.queryForObject(
                "SELECT COUNT(*) FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId), Integer.class);
        return count != null && count > 0;
    }

    @Override
    public UserNoteResponse create(UserNoteRequest request) {
        UUID id = UUID.randomUUID();
        this.jdbc.update("INSERT INTO user_notes (id, user_id, note_id, role) VALUES (:id, :userId, :noteId, :role)",
                Map.of("id", id, "userId", request.userId(), "noteId", request.noteId(), "role",
                        request.role().name()));
        return new UserNoteResponse(id, request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse findByUserNoteId(UUID userNoteId) {
        return this.jdbc
            .query("SELECT id, user_id, note_id, role FROM user_notes WHERE id = :id", Map.of("id", userNoteId),
                    this.userNoteJdbcMapper::fromRow)
            .stream()
            .findFirst()
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        if (!existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        return this.jdbc.query("SELECT id, user_id, note_id, role FROM user_notes WHERE user_id = :userId",
                Map.of("userId", userId), this.userNoteJdbcMapper::fromRow);
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        if (!existsByNoteId(noteId)) {
            throw new NoteNotFoundException(noteId);
        }
        return this.jdbc.query("SELECT id, user_id, note_id, role FROM user_notes WHERE note_id = :noteId",
                Map.of("noteId", noteId), this.userNoteJdbcMapper::fromRow);
    }

    @Override
    public UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.jdbc
            .query("SELECT id, user_id, note_id, role FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                    Map.of("userId", userId, "noteId", noteId), this.userNoteJdbcMapper::fromRow)
            .stream()
            .findFirst()
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
    }

    @Override
    public UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        findByUserNoteId(userNoteId);
        this.jdbc.update("UPDATE user_notes SET user_id = :userId, note_id = :noteId, role = :role WHERE id = :id",
                Map.of("id", userNoteId, "userId", request.userId(), "noteId", request.noteId(), "role",
                        request.role().name()));
        return new UserNoteResponse(userNoteId, request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteResponse existing = findByUserIdAndNoteId(userId, noteId);
        this.jdbc.update(
                "UPDATE user_notes SET user_id = :newUserId, note_id = :newNoteId, role = :role WHERE id = :id",
                Map.of("id", existing.id(), "newUserId", request.userId(), "newNoteId", request.noteId(), "role",
                        request.role().name()));
        return new UserNoteResponse(existing.id(), request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        UserNoteResponse existing = findByUserNoteId(userNoteId);
        UserNoteRequest merged = merge(existing, request);
        this.jdbc.update("UPDATE user_notes SET user_id = :userId, note_id = :noteId, role = :role WHERE id = :id", Map
            .of("id", userNoteId, "userId", merged.userId(), "noteId", merged.noteId(), "role", merged.role().name()));
        return new UserNoteResponse(userNoteId, merged.userId(), merged.noteId(), merged.role());
    }

    @Override
    public UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteResponse existing = findByUserIdAndNoteId(userId, noteId);
        UserNoteRequest merged = merge(existing, request);
        this.jdbc.update(
                "UPDATE user_notes SET user_id = :newUserId, note_id = :newNoteId, role = :role WHERE id = :id",
                Map.of("id", existing.id(), "newUserId", merged.userId(), "newNoteId", merged.noteId(), "role",
                        merged.role().name()));
        return new UserNoteResponse(existing.id(), merged.userId(), merged.noteId(), merged.role());
    }

    @Override
    public UserNoteResponse deleteByUserNoteId(UUID userNoteId) {
        UserNoteResponse existing = findByUserNoteId(userNoteId);
        this.jdbc.update("DELETE FROM user_notes WHERE id = :id", Map.of("id", userNoteId));
        return existing;
    }

    @Override
    public UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId) {
        UserNoteResponse existing = findByUserIdAndNoteId(userId, noteId);
        this.jdbc.update("DELETE FROM user_notes WHERE user_id = :userId AND note_id = :noteId",
                Map.of("userId", userId, "noteId", noteId));
        return existing;
    }

    private static UserNoteRequest merge(UserNoteResponse existing, UserNoteRequest request) {
        UUID userId = (request.userId() != null) ? request.userId() : existing.userId();
        UUID noteId = (request.noteId() != null) ? request.noteId() : existing.noteId();
        UserNoteRole role = (request.role() != null) ? request.role() : existing.role();
        return new UserNoteRequest(userId, noteId, role);
    }

}
