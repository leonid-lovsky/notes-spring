package com.example.usernote;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
public class UserNoteController {

    private final FindUserNoteById findUserNoteById;
    private final CreateUserNote createUserNote;
    private final UpdateUserNoteRoleById updateUserNoteRoleById;
    private final DeleteUserNoteById deleteUserNoteById;

    public UserNoteController(
        FindUserNoteById findUserNoteById,
        CreateUserNote createUserNote,
        UpdateUserNoteRoleById updateUserNoteRoleById,
        DeleteUserNoteById deleteUserNoteById
    ) {
        this.findUserNoteById = findUserNoteById;
        this.createUserNote = createUserNote;
        this.updateUserNoteRoleById = updateUserNoteRoleById;
        this.deleteUserNoteById = deleteUserNoteById;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserNoteResponseBody> findUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(findUserNoteById.findUserNoteById(id));
    }

    @PostMapping
    public ResponseEntity<UserNoteResponseBody> createUserNote(
        @Validated @RequestBody UserNoteRequestBody requestBody
    ) {
        return ResponseEntity.ok(createUserNote.createUserNote(requestBody));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserNoteResponseBody> updateUserNoteRoleById(
        @PathVariable("id") UUID id,
        @RequestBody UserNoteRole role
    ) {
        return ResponseEntity.ok(updateUserNoteRoleById.updateUserNoteRoleById(id, role));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserNoteResponseBody> deleteUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return ResponseEntity.ok(deleteUserNoteById.deleteUserNoteById(id));
    }
}
