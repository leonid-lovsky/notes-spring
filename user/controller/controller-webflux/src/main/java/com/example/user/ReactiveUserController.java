package com.example.user;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class ReactiveUserController {

    private final ReactiveFindUserById findUserById;
    private final ReactiveCreateUserById createUserById;
    private final ReactiveReplaceUserById replaceUserById;
    private final ReactiveUpdateUserById updateUserById;
    private final ReactiveDeleteUserById deleteUserById;

    public ReactiveUserController(
        ReactiveFindUserById findUserById,
        ReactiveCreateUserById createUserById,
        ReactiveReplaceUserById replaceUserById,
        ReactiveUpdateUserById updateUserById,
        ReactiveDeleteUserById deleteUserById
    ) {
        this.findUserById = findUserById;
        this.createUserById = createUserById;
        this.replaceUserById = replaceUserById;
        this.updateUserById = updateUserById;
        this.deleteUserById = deleteUserById;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findUserById(@PathVariable("id") UUID id) {
        return findUserById.findUserById(id).map(ResponseEntity::ok);
    }

    @PostMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> createUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return createUserById.createUserById(id, request).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> replaceUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return replaceUserById.replaceUserById(id, request).map(ResponseEntity::ok);
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return updateUserById.updateUserById(id, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> deleteUserById(@PathVariable("id") UUID id) {
        return deleteUserById.deleteUserById(id).map(ResponseEntity::ok);
    }
}
