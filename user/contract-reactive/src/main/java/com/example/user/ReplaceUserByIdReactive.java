package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReplaceUserByIdReactive {

    Mono<UserResponse> execute(UUID id, UserRequest request);
}
