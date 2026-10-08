package com.example.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveDeleteUserById {

    Mono<UserResponse> deleteUserById(UUID id);
}
