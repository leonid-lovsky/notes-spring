package com.example.noteuser;

import com.example.noteuser.persistence.NoteAccessEntity;
import com.example.noteuser.persistence.NoteAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteAccessService {

    private final NoteAccessRepository noteAccessRepository;

    public void grantCreatorAccess(UUID noteId, UUID userId) {
        NoteAccessEntity noteAccessEntity = new NoteAccessEntity();
        noteAccessEntity.setNoteId(noteId);
        noteAccessEntity.setUserId(userId);
        noteAccessEntity.setRole(NoteAccessRole.CREATOR);
        noteAccessRepository.save(noteAccessEntity);
    }

    @Transactional(readOnly = true)
    public List<UUID> getNoteIdsForUser(UUID userId) {
        return noteAccessRepository.findAllByUserId(userId).stream()
            .map(NoteAccessEntity::getNoteId)
            .toList();
    }

    @Transactional(readOnly = true)
    public void ensureUserCanRead(UUID noteId, UUID userId) {
        NoteAccessRole noteAccessRole = getRequiredRole(noteId, userId);
        if (!noteAccessRole.canRead()) {
            throw accessDenied();
        }
    }

    private NoteAccessRole getRequiredRole(UUID noteId, UUID userId) {
        return noteAccessRepository.findByNoteIdAndUserId(noteId, userId)
            .map(NoteAccessEntity::getRole)
            .orElseThrow(this::accessDenied);
    }

    private ResponseStatusException accessDenied() {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this note.");
    }

    @Transactional(readOnly = true)
    public void ensureUserCanEdit(UUID noteId, UUID userId) {
        NoteAccessRole noteAccessRole = getRequiredRole(noteId, userId);
        if (!noteAccessRole.canEdit()) {
            throw accessDenied();
        }
    }

    @Transactional(readOnly = true)
    public void ensureUserCanDelete(UUID noteId, UUID userId) {
        NoteAccessRole noteAccessRole = getRequiredRole(noteId, userId);
        if (!noteAccessRole.canDelete()) {
            throw accessDenied();
        }
    }

    public void deleteAllAccessForNote(UUID noteId) {
        noteAccessRepository.deleteAllByNoteId(noteId);
    }
}
