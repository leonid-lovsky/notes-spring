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

@RestController
@RequestMapping("/users")
public class UserController {

    private final FindUserByIdSynchronous findUserById;
    private final CreateUserByIdSynchronous createUserById;
    private final ReplaceUserByIdSynchronous replaceUserById;
    private final UpdateUserByIdSynchronous updateUserById;
    private final DeleteUserByIdSynchronous deleteUserById;

    public UserController(FindUserByIdSynchronous findUserById, CreateUserByIdSynchronous createUserById, ReplaceUserByIdSynchronous replaceUserById, UpdateUserByIdSynchronous updateUserById, DeleteUserByIdSynchronous deleteUserById) {
        this.findUserById = findUserById;
        this.createUserById = createUserById;
        this.replaceUserById = replaceUserById;
        this.updateUserById = updateUserById;
        this.deleteUserById = deleteUserById;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(findUserById.execute(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponse> createUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(createUserById.execute(id, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> replaceUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(replaceUserById.execute(id, request));
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable("id") UUID id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(updateUserById.execute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUserById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(deleteUserById.execute(id));
    }
}
