package com.example.usernote;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
public class UserNoteController {

    private final FindUserNoteById findUserNoteById;
    private final CreateUserNoteById createUserNoteById;
    private final ReplaceUserNoteById replaceUserNoteById;
    private final UpdateUserNoteById updateUserNoteById;
    private final DeleteUserNoteById deleteUserNoteById;

    public UserNoteController(FindUserNoteById findUserNoteById, CreateUserNoteById createUserNoteById, ReplaceUserNoteById replaceUserNoteById, UpdateUserNoteById updateUserNoteById, DeleteUserNoteById deleteUserNoteById) {
        this.findUserNoteById = findUserNoteById;
        this.createUserNoteById = createUserNoteById;
        this.replaceUserNoteById = replaceUserNoteById;
        this.updateUserNoteById = updateUserNoteById;
        this.deleteUserNoteById = deleteUserNoteById;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserNoteResponse> findUserNoteById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(findUserNoteById.findUserNoteById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserNoteResponse> createUserNoteById(@PathVariable("id") UUID id, @RequestBody UserNoteRequest request) {
        return ResponseEntity.ok(createUserNoteById.createUserNoteById(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserNoteResponse> replaceUserNoteById(@PathVariable("id") UUID id, @RequestBody UserNoteRequest request) {
        return ResponseEntity.ok(replaceUserNoteById.replaceUserNoteById(id, request));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserNoteResponse> updateUserNoteById(@PathVariable("id") UUID id, @RequestBody UserNoteRequest request) {
        return ResponseEntity.ok(updateUserNoteById.updateUserNoteById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserNoteResponse> deleteUserNoteById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(deleteUserNoteById.deleteUserNoteById(id));
    }
}
