package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteContractReactive;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
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
@RequestMapping("/user-notes")
class UserNoteController {

    private final UserNoteContractReactive userNoteContractReactive;

    UserNoteController(UserNoteContractReactive userNoteContractReactive) {
        this.userNoteContractReactive = userNoteContractReactive;
    }

    @PostMapping
    Mono<ResponseEntity<UserNoteResponse>> create(@RequestBody UserNoteRequest request) {
        return this.userNoteContractReactive.add(request)
            .map((userNote) -> ResponseEntity.status(HttpStatus.CREATED).body(userNote));
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<UserNoteResponse>> findById(@PathVariable UUID id) {
        return this.userNoteContractReactive.findById(id)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote))
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(id)));
    }

    @GetMapping("/note/{noteId}")
    Flux<UserNoteResponse> findByNoteId(@PathVariable UUID noteId) {
        return this.userNoteContractReactive.findByNoteId(noteId);
    }

    @GetMapping("/{userId}/{noteId}")
    Mono<ResponseEntity<UserNoteResponse>> findByUserIdAndNoteId(@PathVariable UUID userId, @PathVariable UUID noteId) {
        return this.userNoteContractReactive.findByUserIdAndNoteId(userId, noteId)
            .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote))
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

    @GetMapping("/user/{userId}")
    Flux<UserNoteResponse> findByUserId(@PathVariable UUID userId) {
        return this.userNoteContractReactive.findByUserId(userId);
    }

    @PutMapping("/{userId}/{noteId}")
    Mono<ResponseEntity<UserNoteResponse>> update(@PathVariable UUID userId, @PathVariable UUID noteId,
            @RequestBody UserNoteRequest request) {
        return this.userNoteContractReactive.existsByUserIdAndNoteId(userId, noteId)
            .flatMap((exists) -> exists
                    ? this.userNoteContractReactive.replace(userId, noteId, request)
                        .map((userNote) -> ResponseEntity.status(HttpStatus.OK).body(userNote))
                    : Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

    @DeleteMapping("/{userId}/{noteId}")
    Mono<ResponseEntity<Void>> delete(@PathVariable UUID userId, @PathVariable UUID noteId) {
        return this.userNoteContractReactive.existsByUserIdAndNoteId(userId, noteId)
            .flatMap((exists) -> exists
                    ? this.userNoteContractReactive.remove(userId, noteId)
                        .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build())
                    : Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

}
