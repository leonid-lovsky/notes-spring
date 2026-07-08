package com.example.user.webflux;

import java.util.UUID;

import com.example.user.contract.reactive.UserContractReactive;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserContractReactive userContractReactive;

    UserController(UserContractReactive userContractReactive) {
        this.userContractReactive = userContractReactive;
    }

    @PostMapping
    Mono<ResponseEntity<UserResponse>> create(@RequestBody UserRequest request) {
        return this.userContractReactive.add(request)
            .map((user) -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @GetMapping
    Flux<UserResponse> findAll() {
        return this.userContractReactive.findAll();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<UserResponse>> findById(@PathVariable UUID id) {
        return this.userContractReactive.findById(id)
            .map((user) -> ResponseEntity.status(HttpStatus.OK).body(user))
            .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<UserResponse>> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        return this.userContractReactive.existsById(id)
            .flatMap((exists) -> exists
                    ? this.userContractReactive.replace(id, request)
                        .map((user) -> ResponseEntity.status(HttpStatus.OK).body(user))
                    : Mono.error(new UserNotFoundException(id)));
    }

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return this.userContractReactive.existsById(id)
            .flatMap((exists) -> exists
                    ? this.userContractReactive.remove(id)
                        .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
                    : Mono.error(new UserNotFoundException(id)));
    }

}
