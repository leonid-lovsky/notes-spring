package com.example.user;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveFindUserById {

    Mono<UserResponse> findUserById(UUID id);
}
