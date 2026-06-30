package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByIdContractReactive;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByIdController {

    private final UserNoteFindByIdContractReactive userNoteFindByIdContractReactive;

    UserNoteFindByIdController(UserNoteFindByIdContractReactive userNoteFindByIdContractReactive) {
        this.userNoteFindByIdContractReactive = userNoteFindByIdContractReactive;
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<UserNoteResponse>> findById(@PathVariable UUID id) {
        return this.userNoteFindByIdContractReactive.findById(id)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote))
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(id)));
    }

}
