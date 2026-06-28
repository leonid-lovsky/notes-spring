package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdAndNoteIdContractReactive;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-notes")
class UserNoteFindByUserIdAndNoteIdController {

    private final UserNoteFindByUserIdAndNoteIdContractReactive userNoteFindByUserIdAndNoteIdContractReactive;

    UserNoteFindByUserIdAndNoteIdController(
            UserNoteFindByUserIdAndNoteIdContractReactive userNoteFindByUserIdAndNoteIdContractReactive) {
        this.userNoteFindByUserIdAndNoteIdContractReactive = userNoteFindByUserIdAndNoteIdContractReactive;
    }

    @GetMapping("/{userId}/{noteId}")
    Mono<ResponseEntity<UserNoteResponse>> findByUserIdAndNoteId(@PathVariable UUID userId,
            @PathVariable UUID noteId) {
        return this.userNoteFindByUserIdAndNoteIdContractReactive.findByUserIdAndNoteId(userId, noteId)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote))
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

}
