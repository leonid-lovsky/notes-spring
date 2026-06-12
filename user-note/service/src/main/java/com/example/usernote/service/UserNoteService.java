package com.example.usernote.service;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteOutputPort;
import com.example.usernote.domain.UserNoteRole;
import com.example.usernote.domain.UserNoteUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
class UserNoteService implements UserNoteUseCase {

    private final UserNoteOutputPort userNoteOutputPort;

    UserNoteService(UserNoteOutputPort userNoteOutputPort) {
        this.userNoteOutputPort = userNoteOutputPort;
    }

    @Override
    public UserNote create(UUID userId, UUID noteId, UserNoteRole role) {
        UserNote userNote = new UserNote(userId, noteId, role);
        userNoteOutputPort.add(userNote);
        return userNote;
    }

    @Override
    @Transactional(readOnly = true)
    public UserNote findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteOutputPort.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new NoSuchElementException(userId + "/" + noteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserNote> findByUserId(UUID userId) {
        return userNoteOutputPort.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserNote> findByNoteId(UUID noteId) {
        return userNoteOutputPort.findByNoteId(noteId);
    }

    @Override
    public UserNote update(UUID userId, UUID noteId, UserNoteRole role) {
        userNoteOutputPort.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new NoSuchElementException(userId + "/" + noteId));
        UserNote updated = new UserNote(userId, noteId, role);
        userNoteOutputPort.replace(updated);
        return updated;
    }

    @Override
    public void delete(UUID userId, UUID noteId) {
        if (!userNoteOutputPort.existsByUserIdAndNoteId(userId, noteId)) {
            throw new NoSuchElementException(userId + "/" + noteId);
        }
        userNoteOutputPort.remove(userId, noteId);
    }
}
