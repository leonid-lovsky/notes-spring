package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRepository;
import com.example.usernote.domain.UserNoteRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteController {

    private final UserNoteRepository userNoteRepository;

    UserNoteController(UserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<UserNoteResponse>> findByUserId(@PathVariable UUID userId) {
        List<UserNoteResponse> userNotes = userNoteRepository.findByUserId(userId).stream()
            .map(UserNoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @GetMapping("/note/{noteId}")
    ResponseEntity<List<UserNoteResponse>> findByNoteId(@PathVariable UUID noteId) {
        List<UserNoteResponse> userNotes = userNoteRepository.findByNoteId(noteId).stream()
            .map(UserNoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @GetMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@PathVariable UUID userId, @PathVariable UUID noteId) {
        UserNote userNote = userNoteRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        return ResponseEntity.status(HttpStatus.OK).body(UserNoteResponse.from(userNote));
    }

    @PostMapping
    ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNote userNote = userNoteRepository.add(new UserNote(request.userId(), request.noteId(), request.role()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserNoteResponse.from(userNote));
    }

    @PutMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> update(@PathVariable UUID userId, @PathVariable UUID noteId, @RequestBody UserNoteRequest request) {
        if (!userNoteRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNote updated = new UserNote(userId, noteId, request.role());
        userNoteRepository.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(UserNoteResponse.from(updated));
    }

    @DeleteMapping("/{userId}/{noteId}")
    ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        if (!userNoteRepository.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        userNoteRepository.remove(userId, noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
