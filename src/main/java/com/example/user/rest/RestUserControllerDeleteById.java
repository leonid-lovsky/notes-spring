package com.example.user.rest;

import com.example.user.UserPayloadResponse;
import com.example.user.UserServiceDeleteById;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@RequestMapping("${application.rest.users.path}")
@RequiredArgsConstructor
class RestUserControllerDeleteById {

    private final UserServiceDeleteById userServiceDeleteById;

    @DeleteMapping("/{id}")
    ResponseEntity<UserPayloadResponse> deleteById(@PathVariable UUID id) {
        UserPayloadResponse userPayloadResponse = userServiceDeleteById.deleteById(id);
        return ResponseEntity.ok(userPayloadResponse);
    }
}
