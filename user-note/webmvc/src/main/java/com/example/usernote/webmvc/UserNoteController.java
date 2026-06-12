package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteController {

    private final UserNoteUseCase userNoteUseCase;

    UserNoteController(UserNoteUseCase userNoteUseCase) {
        this.userNoteUseCase = userNoteUseCase;
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<UserNoteResponse>> findByUserId(@PathVariable UUID userId) {
        List<UserNoteResponse> userNotes = userNoteUseCase.findByUserId(userId).stream()
            .map(UserNoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @GetMapping("/note/{noteId}")
    ResponseEntity<List<UserNoteResponse>> findByNoteId(@PathVariable UUID noteId) {
        List<UserNoteResponse> userNotes = userNoteUseCase.findByNoteId(noteId).stream()
            .map(UserNoteResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @GetMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@PathVariable UUID userId, @PathVariable UUID noteId) {
        UserNoteResponse response = UserNoteResponse.from(userNoteUseCase.findByUserIdAndNoteId(userId, noteId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNote userNote = userNoteUseCase.create(request.userId(), request.noteId(), request.role());
        UserNoteResponse response = UserNoteResponse.from(userNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> update(@PathVariable UUID userId, @PathVariable UUID noteId, @RequestBody UserNoteRequest request) {
        UserNote updated = userNoteUseCase.update(userId, noteId, request.role());
        UserNoteResponse response = UserNoteResponse.from(updated);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{userId}/{noteId}")
    ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        userNoteUseCase.delete(userId, noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Void> handleNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
