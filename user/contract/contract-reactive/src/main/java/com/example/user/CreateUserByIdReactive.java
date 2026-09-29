package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreateUserByIdReactive {

    Mono<UserResponse> execute(UUID id, UserRequest request);
}
