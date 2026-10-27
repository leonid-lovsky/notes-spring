package com.example.usernote;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
public class UserNoteController {

    private final UserNoteService userNoteService;

    public UserNoteController(UserNoteService userNoteService) {
        this.userNoteService = userNoteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserNoteResponseBody> findUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(userNoteService.findUserNoteById(id));
    }

    @PostMapping
    public ResponseEntity<UserNoteResponseBody> createUserNote(
        @Validated @RequestBody UserNoteRequestBody requestBody
    ) {
        return ResponseEntity.ok(userNoteService.createUserNote(requestBody));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserNoteResponseBody> updateUserNoteRoleById(
        @PathVariable("id") UUID id,
        @RequestBody UserNoteRole role
    ) {
        return ResponseEntity.ok(userNoteService.updateUserNoteRoleById(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserNoteResponseBody> deleteUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(userNoteService.deleteUserNoteById(id));
    }
}
