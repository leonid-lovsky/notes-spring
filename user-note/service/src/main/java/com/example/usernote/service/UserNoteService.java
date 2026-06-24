package com.example.usernote.service;

import com.example.usernote.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
class UserNoteService implements UserNoteUseCase {

    private final UserNoteRepository userNoteRepository;

    UserNoteService(UserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public UserNote create(UUID userId, UUID noteId, UserNoteRole role) {
        UserNote userNote = new UserNote(userId, noteId, role);
        userNoteRepository.add(userNote);
        return userNote;
    }

    @Override
    @Transactional(readOnly = true)
    public UserNote findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserNote> findByUserId(UUID userId) {
        return userNoteRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserNote> findByNoteId(UUID noteId) {
        return userNoteRepository.findByNoteId(noteId);
    }

    @Override
    public UserNote update(UUID userId, UUID noteId, UserNoteRole role) {
        if (!userNoteRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNote updated = new UserNote(userId, noteId, role);
        userNoteRepository.replace(updated);
        return updated;
    }

    @Override
    public void delete(UUID userId, UUID noteId) {
        if (!userNoteRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        userNoteRepository.remove(userId, noteId);
    }
}
