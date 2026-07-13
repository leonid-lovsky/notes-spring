package com.example.user.webmvc;

import java.util.List;
import java.util.UUID;

import com.example.user.contract.UserServiceInterface;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;

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

@RestController
@RequestMapping("/users")
class UserController implements UserControllerInterface {

    private final UserServiceInterface userService;

    UserController(UserServiceInterface userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable UUID id) {
        Boolean response = this.userService.existsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<UserResponse> add(@RequestBody UserRequest request) {
        UserResponse response = this.userService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> response = this.userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        UserResponse response = this.userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> replace(@PathVariable UUID id, @RequestBody UserRequest request) {
        UserResponse response = this.userService.replace(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> merge(@PathVariable UUID id, @RequestBody UserRequest request) {
        UserResponse response = this.userService.merge(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> remove(@PathVariable UUID id) {
        UserResponse response = this.userService.remove(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
