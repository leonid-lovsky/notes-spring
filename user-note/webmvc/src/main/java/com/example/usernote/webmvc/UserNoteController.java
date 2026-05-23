package com.example.usernote.webmvc;

import com.example.usernote.domain.NoteClient;
import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
public class UserNoteController {

    private final UserNoteRepository repository;
    private final NoteClient noteClient;

    public UserNoteController(UserNoteRepository repository, NoteClient noteClient) {
        this.repository = repository;
        this.noteClient = noteClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserNote> findById(@PathVariable("id") UUID id, Principal principal) {
        return repository.findById(id)
            .filter(un -> un.userId().equals(principal.getName()) || un.ownerId().equals(principal.getName()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserNote> sharedWithMe(Principal principal) {
        return repository.findByUserId(principal.getName());
    }

    @GetMapping("/owned")
    public List<UserNote> sharedByMe(Principal principal) {
        return repository.findByOwnerId(principal.getName());
    }

    @PostMapping
    public ResponseEntity<UserNote> share(@RequestBody ShareRequest request, Principal principal) {
        var note = noteClient.findById(request.noteId());
        if (note.isEmpty() || !note.get().ownerId().equals(principal.getName())) {
            return ResponseEntity.notFound().build();
        }
        if (repository.findByUserIdAndNoteId(request.userId(), request.noteId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        var userNote = repository.save(
            UserNote.create(request.userId(), request.noteId(), principal.getName(), request.permission())
        );
        return ResponseEntity.created(URI.create("/user-notes/" + userNote.id())).body(userNote);
    }

    @DeleteMapping("/notes/{noteId}/users/{userId}")
    public ResponseEntity<Void> revoke(@PathVariable("noteId") UUID noteId,
                                       @PathVariable("userId") String userId,
                                       Principal principal) {
        repository.findByUserIdAndNoteId(userId, noteId)
            .filter(un -> un.ownerId().equals(principal.getName()))
            .ifPresent(un -> repository.deleteByUserIdAndNoteId(userId, noteId));
        return ResponseEntity.noContent().build();
    }

    record ShareRequest(String userId, UUID noteId, UserNote.Permission permission) {}
}
