package com.example.user.webflux;

import java.util.UUID;

import com.example.user.contract.reactive.UserExistsByIdContractReactive;
import com.example.user.contract.reactive.UserReplaceContractReactive;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserUpdateController {

    private final UserExistsByIdContractReactive userExistsByIdContractReactive;

    private final UserReplaceContractReactive userReplaceContractReactive;

    UserUpdateController(UserExistsByIdContractReactive userExistsByIdContractReactive,
            UserReplaceContractReactive userReplaceContractReactive) {
        this.userExistsByIdContractReactive = userExistsByIdContractReactive;
        this.userReplaceContractReactive = userReplaceContractReactive;
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<UserResponse>> update(@PathVariable UUID id, @RequestBody UserRequest request) {
        return this.userExistsByIdContractReactive.existsById(id)
            .flatMap((exists) -> exists
                    ? this.userReplaceContractReactive.replace(id, request)
                        .map((user) -> ResponseEntity.status(HttpStatus.OK).body(user))
                    : Mono.error(new UserNotFoundException(id)));
    }

}
