package com.example.user.webmvc;

import com.example.user.domain.User;

import java.util.UUID;

record UserResponse(UUID id, String username, String email) {

    static UserResponse from(User user) {
        return new UserResponse(user.id(), user.username(), user.email());
    }
}
