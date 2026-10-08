package com.example.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final FindUserById findUserById;
    private final CreateUserById createUserById;
    private final ReplaceUserById replaceUserById;
    private final UpdateUserById updateUserById;
    private final DeleteUserById deleteUserById;

    public UserController(
        FindUserById findUserById,
        CreateUserById createUserById,
        ReplaceUserById replaceUserById,
        UpdateUserById updateUserById,
        DeleteUserById deleteUserById
    ) {
        this.findUserById = findUserById;
        this.createUserById = createUserById;
        this.replaceUserById = replaceUserById;
        this.updateUserById = updateUserById;
        this.deleteUserById = deleteUserById;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(findUserById.findUserById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponse> createUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(createUserById.createUserById(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> replaceUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(replaceUserById.replaceUserById(id, request));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(updateUserById.updateUserById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUserById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(deleteUserById.deleteUserById(id));
    }
}
