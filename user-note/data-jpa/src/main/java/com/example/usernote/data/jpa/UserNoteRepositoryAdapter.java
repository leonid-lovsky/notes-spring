package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteRepositoryAdapter implements UserNoteRepository {

    private final UserNoteJpaRepository jpa;

    UserNoteRepositoryAdapter(UserNoteJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public UserNote save(UserNote userNote) {
        return toUserNote(jpa.save(toEntity(userNote)));
    }

    @Override
    public List<UserNote> findByUserId(String userId) {
        return jpa.findByUserId(userId).stream().map(this::toUserNote).toList();
    }

    @Override
    public List<UserNote> findByOwnerId(String ownerId) {
        return jpa.findByOwnerId(ownerId).stream().map(this::toUserNote).toList();
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(String userId, UUID noteId) {
        return jpa.findByUserIdAndNoteId(userId, noteId).map(this::toUserNote);
    }

    @Override
    public void deleteByUserIdAndNoteId(String userId, UUID noteId) {
        jpa.deleteByUserIdAndNoteId(userId, noteId);
    }

    private UserNoteEntity toEntity(UserNote u) {
        var e = new UserNoteEntity();
        e.id = u.id();
        e.userId = u.userId();
        e.noteId = u.noteId();
        e.ownerId = u.ownerId();
        e.permission = u.permission();
        return e;
    }

    private UserNote toUserNote(UserNoteEntity e) {
        return new UserNote(e.id, e.userId, e.noteId, e.ownerId, e.permission);
    }
}
