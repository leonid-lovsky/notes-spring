package com.example.usernote;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-notes")
public class ReactiveUserNoteController {

    private final ReactiveFindUserNoteById findUserNoteById;
    private final ReactiveCreateUserNoteById createUserNoteById;
    private final ReactiveReplaceUserNoteById replaceUserNoteById;
    private final ReactiveUpdateUserNoteById updateUserNoteById;
    private final ReactiveDeleteUserNoteById deleteUserNoteById;

    public ReactiveUserNoteController(
        ReactiveFindUserNoteById findUserNoteById,
        ReactiveCreateUserNoteById createUserNoteById,
        ReactiveReplaceUserNoteById replaceUserNoteById,
        ReactiveUpdateUserNoteById updateUserNoteById,
        ReactiveDeleteUserNoteById deleteUserNoteById
    ) {
        this.findUserNoteById = findUserNoteById;
        this.createUserNoteById = createUserNoteById;
        this.replaceUserNoteById = replaceUserNoteById;
        this.updateUserNoteById = updateUserNoteById;
        this.deleteUserNoteById = deleteUserNoteById;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponse>> findUserNoteById(@PathVariable("id") UUID id) {
        return findUserNoteById.findUserNoteById(id).map(ResponseEntity::ok);
    }

    @PostMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponse>> createUserNoteById(@PathVariable("id") UUID id, @RequestBody UserNoteRequest request) {
        return createUserNoteById.createUserNoteById(id, request).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponse>> replaceUserNoteById(@PathVariable("id") UUID id, @RequestBody UserNoteRequest request) {
        return replaceUserNoteById.replaceUserNoteById(id, request).map(ResponseEntity::ok);
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<UserNoteResponse>> updateUserNoteById(@PathVariable("id") UUID id, @RequestBody UserNoteRequest request) {
        return updateUserNoteById.updateUserNoteById(id, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponse>> deleteUserNoteById(@PathVariable("id") UUID id) {
        return deleteUserNoteById.deleteUserNoteById(id).map(ResponseEntity::ok);
    }
}
