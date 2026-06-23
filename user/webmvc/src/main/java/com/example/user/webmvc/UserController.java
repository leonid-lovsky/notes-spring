package com.example.user.webmvc;

import com.example.user.domain.User;
import com.example.user.domain.UserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserUseCase userUseCase;

    UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userUseCase.findAll().stream()
            .map(UserResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse response = UserResponse.from(userUseCase.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        User user = userUseCase.create(request.username(), request.email());
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        User updated = userUseCase.update(id, request.username(), request.email());
        UserResponse response = UserResponse.from(updated);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        userUseCase.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
