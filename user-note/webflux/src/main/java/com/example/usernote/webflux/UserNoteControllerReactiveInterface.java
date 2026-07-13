package com.example.usernote.webflux;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteReactiveInterface;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

public interface UserNoteControllerReactiveInterface extends UserNoteReactiveInterface {

    @Override
    Mono<ResponseEntity<Boolean>> existsByUserNoteId(UUID userNoteId);

    @Override
    Mono<ResponseEntity<Boolean>> existsByUserId(UUID userId);

    @Override
    Mono<ResponseEntity<Boolean>> existsByNoteId(UUID noteId);

    @Override
    Mono<ResponseEntity<Boolean>> existsByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    Mono<ResponseEntity<UserNoteResponse>> add(UserNoteRequest request);

    @Override
    Mono<ResponseEntity<UserNoteResponse>> findByUserNoteId(UUID userNoteId);

    @Override
    ResponseEntity<Flux<UserNoteResponse>> findByUserId(UUID userId);

    @Override
    ResponseEntity<Flux<UserNoteResponse>> findByNoteId(UUID noteId);

    @Override
    Mono<ResponseEntity<UserNoteResponse>> findByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    Mono<ResponseEntity<UserNoteResponse>> replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    Mono<ResponseEntity<UserNoteResponse>> replaceByUserIdAndNoteId(UUID userId, UUID noteId,
            UserNoteRequest request);

    @Override
    Mono<ResponseEntity<UserNoteResponse>> mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    Mono<ResponseEntity<UserNoteResponse>> mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    Mono<ResponseEntity<Void>> deleteByUserNoteId(UUID userNoteId);

    @Override
    Mono<ResponseEntity<Void>> deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
