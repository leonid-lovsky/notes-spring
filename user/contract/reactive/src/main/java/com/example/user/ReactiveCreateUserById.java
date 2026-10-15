package com.example.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveCreateUserById {

    Mono<UserResponse> createUserById(UUID id, UserRequest request);
}
