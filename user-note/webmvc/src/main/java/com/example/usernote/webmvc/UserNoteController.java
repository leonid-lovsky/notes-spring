package com.example.usernote.webmvc;

import com.example.usernote.contract.UserNoteServiceInterface;
import com.example.usernote.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
class UserNoteController implements UserNoteControllerInterface {

    private final UserNoteServiceInterface userNoteService;

    UserNoteController(UserNoteServiceInterface userNoteService) {
        this.userNoteService = userNoteService;
    }

    @Override
    @GetMapping("/{userNoteId}/exists")
    public ResponseEntity<Boolean> existsByUserNoteId(@PathVariable UUID userNoteId) {
        Boolean response = this.userNoteService.existsByUserNoteId(userNoteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(path = "/exists", params = "userId")
    public ResponseEntity<Boolean> existsByUserId(@RequestParam UUID userId) {
        Boolean response = this.userNoteService.existsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(path = "/exists", params = "noteId")
    public ResponseEntity<Boolean> existsByNoteId(@RequestParam UUID noteId) {
        Boolean response = this.userNoteService.existsByNoteId(noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping(path = "/exists", params = { "userId", "noteId" })
    public ResponseEntity<Boolean> existsByUserIdAndNoteId(@RequestParam UUID userId, @RequestParam UUID noteId) {
        Boolean response = this.userNoteService.existsByUserIdAndNoteId(userId, noteId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
    @GetMapping(params = { "userId", "noteId" })
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
