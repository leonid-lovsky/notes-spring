package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveDeleteUserById {

    Mono<UserResponse> deleteUserById(UUID id);
}
