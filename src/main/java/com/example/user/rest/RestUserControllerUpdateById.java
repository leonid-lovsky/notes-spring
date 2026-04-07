package com.example.user.rest;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceUpdateById;
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
class RestUserControllerUpdateById {

    private final UserServiceUpdateById userServiceUpdateById;

    @PatchMapping("/{id}")
    ResponseEntity<UserPayloadResponse> updateById(@PathVariable UUID id, @Valid @RequestBody UserPayloadRequest userPayloadRequest) {
        UserPayloadResponse userPayloadResponse = userServiceUpdateById.updateById(id, userPayloadRequest);
        return ResponseEntity.ok(userPayloadResponse);
    }
}
