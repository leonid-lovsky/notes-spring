package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface FindUserById {

    Mono<UserResponse> findUserById(UUID id);
}
