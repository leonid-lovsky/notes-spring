package com.example.user.webflux;

import java.util.UUID;

import com.example.user.contract.reactive.UserExistsByIdContractReactive;
import com.example.user.contract.reactive.UserRemoveContractReactive;
import com.example.user.domain.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
class UserDeleteController {

    private final UserExistsByIdContractReactive userExistsByIdContractReactive;

    private final UserRemoveContractReactive userRemoveContractReactive;

    UserDeleteController(UserExistsByIdContractReactive userExistsByIdContractReactive,
            UserRemoveContractReactive userRemoveContractReactive) {
        this.userExistsByIdContractReactive = userExistsByIdContractReactive;
        this.userRemoveContractReactive = userRemoveContractReactive;
    }

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return this.userExistsByIdContractReactive.existsById(id)
            .flatMap((exists) -> exists
                    ? this.userRemoveContractReactive.remove(id)
                        .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
                    : Mono.error(new UserNotFoundException(id)));
    }

}
