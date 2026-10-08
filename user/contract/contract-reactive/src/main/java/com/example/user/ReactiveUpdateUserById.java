package com.example.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveUpdateUserById {

    Mono<UserResponse> updateUserById(UUID id, UserRequest request);
}
