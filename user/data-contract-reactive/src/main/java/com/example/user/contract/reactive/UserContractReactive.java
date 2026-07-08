package com.example.user.contract.reactive;

import java.util.UUID;

import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserContractReactive {

    Mono<UserResponse> add(UserRequest request);

    Mono<Boolean> existsById(UUID id);

    Flux<UserResponse> findAll();

    Mono<UserResponse> findByEmail(String email);

    Mono<UserResponse> findById(UUID id);

    Mono<UserResponse> findByUsername(String username);

    Mono<Void> remove(UUID id);

    Mono<UserResponse> replace(UUID id, UserRequest request);

}
