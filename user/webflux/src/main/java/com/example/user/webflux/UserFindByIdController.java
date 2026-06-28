package com.example.user.webflux;

import java.util.UUID;

import com.example.user.contract.reactive.UserFindByIdContractReactive;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserFindByIdController {

    private final UserFindByIdContractReactive userFindByIdContractReactive;

    UserFindByIdController(UserFindByIdContractReactive userFindByIdContractReactive) {
        this.userFindByIdContractReactive = userFindByIdContractReactive;
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<UserResponse>> findById(@PathVariable UUID id) {
        return this.userFindByIdContractReactive.findById(id)
            .map((user) -> ResponseEntity.status(HttpStatus.OK).body(user))
            .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

}
