package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteUserByIdReactive {

    Mono<UserResponse> execute(UUID id);
}
