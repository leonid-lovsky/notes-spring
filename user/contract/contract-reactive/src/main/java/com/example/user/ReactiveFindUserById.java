package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveFindUserById {

    Mono<UserResponse> findUserById(UUID id);
}
