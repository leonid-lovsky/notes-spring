package com.example.user.webflux;

import java.util.UUID;

import com.example.user.contract.reactive.UserReactiveInterface;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

public interface UserControllerReactiveInterface extends UserReactiveInterface {

    @Override
    Mono<ResponseEntity<Boolean>> existsById(UUID id);

    @Override
    Mono<ResponseEntity<UserResponse>> add(UserRequest request);

    @Override
    ResponseEntity<Flux<UserResponse>> findAll();

    @Override
    Mono<ResponseEntity<UserResponse>> findById(UUID id);

    @Override
    Mono<ResponseEntity<UserResponse>> replace(UUID id, UserRequest request);

    @Override
    Mono<ResponseEntity<UserResponse>> merge(UUID id, UserRequest request);

    @Override
    Mono<ResponseEntity<Void>> remove(UUID id);
}
