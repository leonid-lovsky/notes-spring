package com.example.user;

import java.util.UUID;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        throw notImplemented();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> findUserByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> replaceUserByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUserByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserByID(@PathVariable("id") UUID id) {
        throw notImplemented();
    }

    private static ResponseStatusException notImplemented() {
        return new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
