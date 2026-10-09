package com.example.usernote;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/user-notes")
public class ReactiveUserNoteController {

    private final ReactiveUserNoteService userNoteService;

    public ReactiveUserNoteController(ReactiveUserNoteService userNoteService) {
        this.userNoteService = userNoteService;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponseBody>> findUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return userNoteService.findUserNoteById(id).map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<UserNoteResponseBody>> createUserNote(
        @Validated @RequestBody UserNoteRequestBody requestBody
    ) {
        return userNoteService.createUserNote(requestBody).map(ResponseEntity::ok);
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<UserNoteResponseBody>> updateUserNoteRoleById(
        @PathVariable("id") UUID id,
        @RequestBody UserNoteRole role
    ) {
        return userNoteService.updateUserNoteRoleById(id, role).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<UserNoteResponseBody>> deleteUserNoteById(
        @PathVariable("id") UUID id
    ) {
        return userNoteService.deleteUserNoteById(id).map(ResponseEntity::ok);
    }
}
