package com.example.usernote.webmvc;

import com.example.usernote.contract.UserNoteService;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteController {

    private final UserNoteService userNoteService;

    UserNoteController(UserNoteService userNoteService) {
        this.userNoteService = userNoteService;
    }

    @PostMapping
    ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNoteResponse response = this.userNoteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userNoteId}")
    ResponseEntity<UserNoteResponse> findByUserNoteId(@PathVariable UUID userNoteId) {
        if (!this.userNoteService.existsByUserNoteId(userNoteId)) {
            throw new UserNoteNotFoundException(userNoteId);
        }
        UserNoteResponse response = this.userNoteService.findByUserNoteId(userNoteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    ResponseEntity<List<UserNoteResponse>> findByUserId(@RequestParam UUID userId) {
        if (!this.userNoteService.existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        List<UserNoteResponse> response = this.userNoteService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    ResponseEntity<List<UserNoteResponse>> findByNoteId(@RequestParam UUID noteId) {
        if (!this.userNoteService.existsByNoteId(noteId)) {
            throw new NoteNotFoundException(noteId);
        }
        List<UserNoteResponse> response = this.userNoteService.findByNoteId(noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId) {
        if (!this.userNoteService.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNoteResponse response = this.userNoteService.findByUserIdAndNoteId(userId, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{userNoteId}")
    ResponseEntity<UserNoteResponse> replaceByUserNoteId(@PathVariable UUID userNoteId, @RequestBody UserNoteRequest request) {
        if (!this.userNoteService.existsByUserNoteId(userNoteId)) {
            throw new UserNoteNotFoundException(userNoteId);
        }
        UserNoteResponse response = this.userNoteService.replaceByUserNoteId(userNoteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping
    ResponseEntity<UserNoteResponse> replaceByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId, @RequestBody UserNoteRequest request) {
        if (!this.userNoteService.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNoteResponse response = this.userNoteService.replaceByUserIdAndNoteId(userId, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{userNoteId}")
    ResponseEntity<UserNoteResponse> mergeByUserNoteId(@PathVariable UUID userNoteId, @RequestBody UserNoteRequest request) {
        if (!this.userNoteService.existsByUserNoteId(userNoteId)) {
            throw new UserNoteNotFoundException(userNoteId);
        }
        UserNoteResponse response = this.userNoteService.mergeByUserNoteId(userNoteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping
    ResponseEntity<UserNoteResponse> mergeByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId, @RequestBody UserNoteRequest request) {
        if (!this.userNoteService.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNoteResponse response = this.userNoteService.mergeByUserIdAndNoteId(userId, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{userNoteId}")
    ResponseEntity<Void> deleteByUserNoteId(@PathVariable UUID userNoteId) {
        if (!this.userNoteService.existsByUserNoteId(userNoteId)) {
            throw new UserNoteNotFoundException(userNoteId);
        }
        this.userNoteService.deleteByUserNoteId(userNoteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    ResponseEntity<UserNoteResponse> deleteByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId) {
        if (!this.userNoteService.existsByUserIdAndNoteId(userId, noteId)) {
            throw new UserNoteNotFoundException(userId, noteId);
        }
        UserNoteResponse response = this.userNoteService.deleteByUserIdAndNoteId(userId, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
