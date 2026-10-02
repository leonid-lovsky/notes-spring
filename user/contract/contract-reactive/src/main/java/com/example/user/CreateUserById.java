package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreateUserById {

    Mono<UserResponse> createUserById(UUID id, UserRequest request);
}
