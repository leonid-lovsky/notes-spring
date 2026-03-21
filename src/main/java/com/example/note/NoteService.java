package com.example.note;

import com.example.note.entity.NoteEntity;
import com.example.note.mapper.NoteMapper;
import com.example.note.payload.NoteRequest;
import com.example.note.payload.NoteResponse;
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
    public List<NoteResponse> list() {
        AuthenticatedUser currentUser = userService.getCurrentUser();
        List<UUID> noteIds = noteAccessService.getNoteIdsForUser(currentUser.id());
        if (noteIds.isEmpty()) {
            return List.of();
        }
        return noteRepository.findAllById(noteIds).stream()
            .map(noteMapper::toResponse)
            .toList();
    }

    public NoteResponse create(NoteRequest request) {
        AuthenticatedUser currentUser = userService.getCurrentUser();
        NoteEntity noteEntity = noteMapper.createEntity(request);
        NoteEntity savedNote = noteRepository.save(noteEntity);
        noteAccessService.grantCreatorAccess(savedNote.getId(), currentUser.id());
        return noteMapper.toResponse(savedNote);
    }

    @Transactional(readOnly = true)
    public NoteResponse read(UUID noteId) {
        NoteEntity noteEntity = findNote(noteId);
        noteAccessService.ensureUserCanRead(noteId, userService.getCurrentUser().id());
        return noteMapper.toResponse(noteEntity);
    }

    private NoteEntity findNote(UUID noteId) {
        return noteRepository.findById(noteId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found."));
    }

    public NoteResponse update(UUID noteId, UpdateNoteRequest request) {
        NoteEntity noteEntity = findNote(noteId);
        noteAccessService.ensureUserCanEdit(noteId, userService.getCurrentUser().id());
        noteMapper.updateEntity(request, noteEntity);
        NoteEntity updatedNote = noteRepository.save(noteEntity);
        return noteMapper.toResponse(updatedNote);
    }

    public NoteResponse replace(UUID noteId, ReplaceNoteRequest request) {
        NoteEntity noteEntity = findNote(noteId);
        noteAccessService.ensureUserCanEdit(noteId, userService.getCurrentUser().id());
        noteMapper.replaceEntity(request, noteEntity);
        NoteEntity replacedNote = noteRepository.save(noteEntity);
        return noteMapper.toResponse(replacedNote);
    }

    public void delete(UUID noteId) {
        NoteEntity noteEntity = findNote(noteId);
        noteAccessService.ensureUserCanDelete(noteId, userService.getCurrentUser().id());
        noteAccessService.deleteAllAccessForNote(noteId);
        noteRepository.delete(noteEntity);
    }
}
