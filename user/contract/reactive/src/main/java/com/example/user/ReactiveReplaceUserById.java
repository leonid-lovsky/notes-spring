package com.example.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveReplaceUserById {

    Mono<UserResponse> replaceUserById(UUID id, UserRequest request);
}
