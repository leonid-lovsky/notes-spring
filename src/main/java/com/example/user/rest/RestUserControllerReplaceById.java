package com.example.user.rest;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceReplaceById;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.rest.users.path}")
@RequiredArgsConstructor
class RestUserControllerReplaceById {

    private final UserServiceReplaceById userServiceReplaceById;

    @PutMapping("/{id}")
    ResponseEntity<UserPayloadResponse> replaceById(@PathVariable UUID id, @Valid @RequestBody UserPayloadRequest userPayloadRequest) {
        UserPayloadResponse userPayloadResponse = userServiceReplaceById.replaceById(id, userPayloadRequest);
        return ResponseEntity.ok(userPayloadResponse);
    }
}
