package com.example.user.contract.reactive;

import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserServiceReactiveInterface extends UserReactiveInterface {

    @Override
    Mono<Boolean> existsById(UUID id);

    @Override
    Mono<UserResponse> add(UserRequest request);

    @Override
    Flux<UserResponse> findAll();

    @Override
    Mono<UserResponse> findById(UUID id);

    Mono<UserResponse> findByEmail(String email);

    Mono<UserResponse> findByUsername(String username);

    @Override
    Mono<UserResponse> replace(UUID id, UserRequest request);

    @Override
    Mono<UserResponse> merge(UUID id, UserRequest request);

    @Override
    Mono<Void> remove(UUID id);
}
