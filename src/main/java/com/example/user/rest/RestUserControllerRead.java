package com.example.user.rest;

import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceRead;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("${application.rest.users.path}")
@RequiredArgsConstructor
class RestUserControllerRead {

    private final UserServiceRead userServiceRead;

    @GetMapping
    ResponseEntity<List<UserPayloadResponse>> read() {
        List<UserPayloadResponse> userPayloadResponse = userServiceRead.read();
        return ResponseEntity.ok(userPayloadResponse);
    }
}
