package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveUpdateUserById {

    Mono<UserResponse> updateUserById(UUID id, UserRequest request);
}
