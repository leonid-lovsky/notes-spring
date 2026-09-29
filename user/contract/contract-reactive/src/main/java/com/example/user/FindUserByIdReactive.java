package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface FindUserByIdReactive {

    Mono<UserResponse> execute(UUID id);
}
