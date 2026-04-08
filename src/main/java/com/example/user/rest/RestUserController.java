package com.example.user.rest;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import com.example.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.rest.users.path}")
@RequiredArgsConstructor
class RestUserController {

    private final UserService userService;

    @PostMapping
    ResponseEntity<UserPayloadResponse> create(@Valid @RequestBody UserPayloadRequest userPayloadRequest) {
        UserPayloadResponse userPayloadResponse = userService.create(userPayloadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPayloadResponse);
    }

    @GetMapping
    ResponseEntity<List<UserPayloadResponse>> read() {
        List<UserPayloadResponse> userPayloadResponse = userService.read();
        return ResponseEntity.ok(userPayloadResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<UserPayloadResponse> readById(@PathVariable UUID id) {
        UserPayloadResponse userPayloadResponse = userService.readById(id);
        return ResponseEntity.ok(userPayloadResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserPayloadResponse> updateById(@PathVariable UUID id, @Valid @RequestBody UserPayloadRequest userPayloadRequest) {
        UserPayloadResponse userPayloadResponse = userService.updateById(id, userPayloadRequest);
        return ResponseEntity.ok(userPayloadResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserPayloadResponse> replaceById(@PathVariable UUID id, @Valid @RequestBody UserPayloadRequest userPayloadRequest) {
        UserPayloadResponse userPayloadResponse = userService.replaceById(id, userPayloadRequest);
        return ResponseEntity.ok(userPayloadResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<UserPayloadResponse> deleteById(@PathVariable UUID id) {
        UserPayloadResponse userPayloadResponse = userService.deleteById(id);
        return ResponseEntity.ok(userPayloadResponse);
    }
}
