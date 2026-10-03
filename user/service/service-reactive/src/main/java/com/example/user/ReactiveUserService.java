package com.example.user;

import java.util.UUID;
import reactor.core.publisher.Mono;

public class ReactiveUserService implements ReactiveFindUserById, ReactiveCreateUserById, ReactiveReplaceUserById, ReactiveUpdateUserById, ReactiveDeleteUserById {

    private final ReactiveUserRepository userRepository;

    public ReactiveUserService(ReactiveUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserResponse> findUserById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> createUserById(UUID id, UserRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> replaceUserById(UUID id, UserRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> updateUserById(UUID id, UserRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> deleteUserById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
