package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteExistsByUserIdAndNoteIdContractReactive;
import com.example.usernote.contract.reactive.UserNoteRemoveContractReactive;
import com.example.usernote.domain.UserNoteNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-notes")
class UserNoteDeleteController {

    private final UserNoteExistsByUserIdAndNoteIdContractReactive userNoteExistsByUserIdAndNoteIdContractReactive;

    private final UserNoteRemoveContractReactive userNoteRemoveContractReactive;

    UserNoteDeleteController(
            UserNoteExistsByUserIdAndNoteIdContractReactive userNoteExistsByUserIdAndNoteIdContractReactive,
            UserNoteRemoveContractReactive userNoteRemoveContractReactive) {
        this.userNoteExistsByUserIdAndNoteIdContractReactive = userNoteExistsByUserIdAndNoteIdContractReactive;
        this.userNoteRemoveContractReactive = userNoteRemoveContractReactive;
    }

    @DeleteMapping("/{userId}/{noteId}")
    Mono<ResponseEntity<Void>> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        return this.userNoteExistsByUserIdAndNoteIdContractReactive.existsByUserIdAndNoteId(userId, noteId)
            .flatMap((exists) -> exists
                    ? this.userNoteRemoveContractReactive.remove(userId, noteId)
                        .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
                    : Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

}
