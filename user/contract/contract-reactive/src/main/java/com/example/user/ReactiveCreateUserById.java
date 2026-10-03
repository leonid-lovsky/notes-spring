package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveCreateUserById {

    Mono<UserResponse> createUserById(UUID id, UserRequest request);
}
