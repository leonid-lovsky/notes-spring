package com.example.usernote.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteContract;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteController {

    private final UserNoteContract userNoteContract;

    UserNoteController(UserNoteContract userNoteContract) {
        this.userNoteContract = userNoteContract;
    }

    @PostMapping
    ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNoteResponse userNote = this.userNoteContract.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userNote);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserNoteResponse> findById(@PathVariable UUID id) {
        UserNoteResponse userNote = this.userNoteContract.findById(id)
            .orElseThrow(() -> new UserNoteNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(userNote);
    }

    @GetMapping("/note/{noteId}")
    ResponseEntity<List<UserNoteResponse>> findByNoteId(@PathVariable UUID noteId) {
        List<UserNoteResponse> userNotes = this.userNoteContract.findByNoteId(noteId);
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @GetMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@PathVariable UUID userId, @PathVariable UUID noteId) {
        UserNoteResponse userNote = this.userNoteContract.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        return ResponseEntity.status(HttpStatus.OK).body(userNote);
    }

    @GetMapping("/user/{userId}")
    ResponseEntity<List<UserNoteResponse>> findByUserId(@PathVariable UUID userId) {
        List<UserNoteResponse> userNotes = this.userNoteContract.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userNotes);
    }

    @PutMapping("/{userId}/{noteId}")
    ResponseEntity<UserNoteResponse> update(@PathVariable UUID userId, @PathVariable UUID noteId,
            @RequestBody UserNoteRequest request) {
        if (!this.userNoteContract.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNoteResponse updated = this.userNoteContract.replace(userId, noteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{userId}/{noteId}")
    ResponseEntity<Void> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        if (!this.userNoteContract.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        this.userNoteContract.remove(userId, noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
