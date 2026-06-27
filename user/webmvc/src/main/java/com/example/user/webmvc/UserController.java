package com.example.user.webmvc;

import com.example.user.domain.User;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userRepository.findAll().stream()
            .map(UserResponse::from)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.from(user));
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        User user = userRepository.add(new User(null, request.username(), request.email()));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        User updated = new User(id, request.username(), request.email());
        userRepository.replace(updated);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
