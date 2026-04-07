package com.example.user.rest;

import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceReadById;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.rest.users.path}")
@RequiredArgsConstructor
class RestUserControllerReadById {

    private final UserServiceReadById userServiceReadById;

    @GetMapping("/{id}")
    ResponseEntity<UserPayloadResponse> readById(@PathVariable UUID id) {
        UserPayloadResponse userPayloadResponse = userServiceReadById.readById(id);
        return ResponseEntity.ok(userPayloadResponse);
    }
}
