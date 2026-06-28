package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteExistsByUserIdAndNoteIdContractReactive;
import com.example.usernote.contract.reactive.UserNoteReplaceContractReactive;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-notes")
class UserNoteUpdateController {

    private final UserNoteExistsByUserIdAndNoteIdContractReactive userNoteExistsByUserIdAndNoteIdContractReactive;

    private final UserNoteReplaceContractReactive userNoteReplaceContractReactive;

    UserNoteUpdateController(
            UserNoteExistsByUserIdAndNoteIdContractReactive userNoteExistsByUserIdAndNoteIdContractReactive,
            UserNoteReplaceContractReactive userNoteReplaceContractReactive) {
        this.userNoteExistsByUserIdAndNoteIdContractReactive = userNoteExistsByUserIdAndNoteIdContractReactive;
        this.userNoteReplaceContractReactive = userNoteReplaceContractReactive;
    }

    @PutMapping("/{userId}/{noteId}")
    Mono<ResponseEntity<UserNoteResponse>> update(@PathVariable UUID userId, @PathVariable UUID noteId,
            @RequestBody UserNoteRequest request) {
        return this.userNoteExistsByUserIdAndNoteIdContractReactive.existsByUserIdAndNoteId(userId, noteId)
            .flatMap((exists) -> exists
                    ? this.userNoteReplaceContractReactive.replace(userId, noteId, request)
                        .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote))
                    : Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

}
