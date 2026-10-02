package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReplaceUserById {

    Mono<UserResponse> replaceUserById(UUID id, UserRequest request);
}
