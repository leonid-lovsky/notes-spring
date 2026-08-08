package com.example.user.webflux;

import com.example.user.contract.reactive.UserReactiveInterface;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

public interface UserControllerReactiveInterface extends UserReactiveInterface<
    Mono<ResponseEntity<Boolean>>, Mono<ResponseEntity<UserResponse>>, ResponseEntity<Flux<UserResponse>>,
    Mono<ResponseEntity<Void>>> {

}
