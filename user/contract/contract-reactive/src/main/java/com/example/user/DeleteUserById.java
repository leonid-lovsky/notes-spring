package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteUserById {

    Mono<UserResponse> deleteUserById(UUID id);
}
