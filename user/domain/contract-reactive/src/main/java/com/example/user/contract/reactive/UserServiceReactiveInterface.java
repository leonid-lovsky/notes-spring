package com.example.user.contract.reactive;

import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserServiceReactiveInterface extends
    UserReactiveInterface<Mono<Boolean>, Mono<UserResponse>, Flux<UserResponse>, Mono<Void>> {

    Mono<UserResponse> findByEmail(String email);

    Mono<UserResponse> findByUsername(String username);
}
