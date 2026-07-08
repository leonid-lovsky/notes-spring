package com.example.user.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.user.contract.UserContract;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserContract userContract;

    UserController(UserContract userContract) {
        this.userContract = userContract;
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        UserResponse user = this.userContract.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = this.userContract.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse user = this.userContract.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        if (!this.userContract.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        UserResponse updated = this.userContract.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!this.userContract.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        this.userContract.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
