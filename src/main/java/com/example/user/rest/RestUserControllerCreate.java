package com.example.user.rest;

import com.example.user.UserPayloadRequest;
import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("${application.rest.users.path}")
@RequiredArgsConstructor
class RestUserControllerCreate {

    private final UserServiceCreate userServiceCreate;

    @PostMapping
    ResponseEntity<UserPayloadResponse> create(@Valid @RequestBody UserPayloadRequest userPayloadRequest) {
        UserPayloadResponse userPayloadResponse = userServiceCreate.create(userPayloadRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userPayloadResponse);
    }
}
