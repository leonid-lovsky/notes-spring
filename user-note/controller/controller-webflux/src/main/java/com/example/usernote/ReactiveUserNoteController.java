package com.example.usernote;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
public class ReactiveUserNoteController {

    private final ReactiveFindUserNoteById findUserNoteById;
    private final ReactiveCreateUserNote createUserNote;
    private final ReactiveUpdateUserNoteRoleById updateUserNoteRoleById;
    private final ReactiveDeleteUserNoteById deleteUserNoteById;

    public ReactiveUserNoteController(
        ReactiveFindUserNoteById findUserNoteById,
        ReactiveCreateUserNote createUserNote,
        ReactiveUpdateUserNoteRoleById updateUserNoteRoleById,
        ReactiveDeleteUserNoteById deleteUserNoteById
    ) {
        this.findUserNoteById = findUserNoteById;
        this.createUserNote = createUserNote;
        this.updateUserNoteRoleById = updateUserNoteRoleById;
        this.deleteUserNoteById = deleteUserNoteById;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponseBody>> findUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return findUserNoteById.findUserNoteById(id).map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<UserNoteResponseBody>> createUserNote(
        @Validated @RequestBody UserNoteRequestBody requestBody
    ) {
        return createUserNote.createUserNote(requestBody).map(ResponseEntity::ok);
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<UserNoteResponseBody>> updateUserNoteRoleById(
        @PathVariable("id") UUID id,
        @RequestBody UserNoteRole role
    ) {
        return updateUserNoteRoleById.updateUserNoteRoleById(id, role).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponseBody>> deleteUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return deleteUserNoteById.deleteUserNoteById(id).map(ResponseEntity::ok);
    }
}
