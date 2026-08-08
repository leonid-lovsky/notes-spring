package com.example.usernote.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteServiceInterface;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteController implements UserNoteControllerInterface {

    private final UserNoteServiceInterface userNoteService;

    UserNoteController(UserNoteServiceInterface userNoteService) {
        this.userNoteService = userNoteService;
    }

    @Override
    @PostMapping
    public ResponseEntity<UserNoteResponse> create(@RequestBody UserNoteRequest request) {
        UserNoteResponse response = this.userNoteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping("/{userNoteId}")
    public ResponseEntity<UserNoteResponse> findByUserNoteId(@PathVariable UUID userNoteId) {
        UserNoteResponse response = this.userNoteService.findByUserNoteId(userNoteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(params = "userId")
    public ResponseEntity<List<UserNoteResponse>> findByUserId(@RequestParam UUID userId) {
        List<UserNoteResponse> response = this.userNoteService.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(params = "noteId")
    public ResponseEntity<List<UserNoteResponse>> findByNoteId(@RequestParam UUID noteId) {
        List<UserNoteResponse> response = this.userNoteService.findByNoteId(noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(params = {"userId", "noteId"})
    public ResponseEntity<UserNoteResponse> findByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId) {
        UserNoteResponse response = this.userNoteService.findByUserIdAndNoteId(userId, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/{userNoteId}")
    public ResponseEntity<UserNoteResponse> replaceByUserNoteId(@PathVariable UUID userNoteId, @RequestBody UserNoteRequest request) {
        UserNoteResponse response = this.userNoteService.replaceByUserNoteId(userNoteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping
    public ResponseEntity<UserNoteResponse> replaceByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId, @RequestBody UserNoteRequest request) {
        UserNoteResponse response = this.userNoteService.replaceByUserIdAndNoteId(userId, noteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping("/{userNoteId}")
    public ResponseEntity<UserNoteResponse> mergeByUserNoteId(@PathVariable UUID userNoteId, @RequestBody UserNoteRequest request) {
        UserNoteResponse response = this.userNoteService.mergeByUserNoteId(userNoteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping
    public ResponseEntity<UserNoteResponse> mergeByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId, @RequestBody UserNoteRequest request) {
        UserNoteResponse response = this.userNoteService.mergeByUserIdAndNoteId(userId, noteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/{userNoteId}")
    public ResponseEntity<UserNoteResponse> deleteByUserNoteId(@PathVariable UUID userNoteId) {
        UserNoteResponse response = this.userNoteService.deleteByUserNoteId(userNoteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<UserNoteResponse> deleteByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId) {
        UserNoteResponse response = this.userNoteService.deleteByUserIdAndNoteId(userId, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
