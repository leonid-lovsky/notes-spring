package com.example.note;

import com.example.note.mapping.NoteMapper;
import com.example.note.persistence.NoteEntity;
import com.example.note.persistence.NoteRepository;
import com.example.noteuser.NoteAccessService;
import com.example.user.AuthenticatedUser;
import com.example.user.UserService;
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
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final NoteAccessService noteAccessService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<NoteView> getCurrentUserNotes() {
        AuthenticatedUser currentUser = userService.getCurrentUser();
        List<UUID> noteIds = noteAccessService.getNoteIdsForUser(currentUser.id());
        if (noteIds.isEmpty()) {
            return List.of();
        }
        return noteRepository.findAllByIdInOrderByUpdatedAtDesc(noteIds).stream()
            .map(noteMapper::toView)
            .toList();
    }

    public NoteView createNote(CreateNoteCommand command) {
        AuthenticatedUser currentUser = userService.getCurrentUser();
        NoteEntity noteEntity = noteMapper.createCommandToEntity(command);
        NoteEntity savedNote = noteRepository.save(noteEntity);
        noteAccessService.grantCreatorAccess(savedNote.getId(), currentUser.id());
        return noteMapper.toView(savedNote);
    }

    @Transactional(readOnly = true)
    public NoteView getNote(UUID noteId) {
        NoteEntity noteEntity = findNote(noteId);
        noteAccessService.ensureUserCanRead(noteId, userService.getCurrentUser().id());
        return noteMapper.toView(noteEntity);
    }

    private NoteEntity findNote(UUID noteId) {
        return noteRepository.findById(noteId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found."));
    }

    public NoteView updateNote(UUID noteId, UpdateNoteCommand command) {
        NoteEntity noteEntity = findNote(noteId);
        noteAccessService.ensureUserCanEdit(noteId, userService.getCurrentUser().id());
        noteMapper.updateEntity(command, noteEntity);
        NoteEntity updatedNote = noteRepository.save(noteEntity);
        return noteMapper.toView(updatedNote);
    }

    public void deleteNote(UUID noteId) {
        NoteEntity noteEntity = findNote(noteId);
        noteAccessService.ensureUserCanDelete(noteId, userService.getCurrentUser().id());
        noteAccessService.deleteAllAccessForNote(noteId);
        noteRepository.delete(noteEntity);
    }
}
