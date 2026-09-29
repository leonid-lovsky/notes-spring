package com.example.user;

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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final FindUserByIdReactive findUserById;
    private final CreateUserByIdReactive createUserById;
    private final ReplaceUserByIdReactive replaceUserById;
    private final UpdateUserByIdReactive updateUserById;
    private final DeleteUserByIdReactive deleteUserById;

    public UserController(FindUserByIdReactive findUserById, CreateUserByIdReactive createUserById, ReplaceUserByIdReactive replaceUserById, UpdateUserByIdReactive updateUserById, DeleteUserByIdReactive deleteUserById) {
        this.findUserById = findUserById;
        this.createUserById = createUserById;
        this.replaceUserById = replaceUserById;
        this.updateUserById = updateUserById;
        this.deleteUserById = deleteUserById;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findUserById(@PathVariable("id") UUID id) {
        return findUserById.execute(id).map(ResponseEntity::ok);
    }

    @PostMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> createUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return createUserById.execute(id, request).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> replaceUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return replaceUserById.execute(id, request).map(ResponseEntity::ok);
    }

    @PatchMapping(path = "/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return updateUserById.execute(id, request).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> deleteUserById(@PathVariable("id") UUID id) {
        return deleteUserById.execute(id).map(ResponseEntity::ok);
    }
}
