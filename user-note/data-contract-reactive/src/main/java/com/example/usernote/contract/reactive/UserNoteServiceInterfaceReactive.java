package com.example.usernote.contract.reactive;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserNoteServiceInterfaceReactive extends UserNoteInterfaceReactive {

    @Override
    Mono<Boolean> existsByUserNoteId(UUID userNoteId);

    @Override
    Mono<Boolean> existsByUserId(UUID userId);

    @Override
    Mono<Boolean> existsByNoteId(UUID noteId);

    @Override
    Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    Mono<UserNoteResponse> add(UserNoteRequest request);

    @Override
    Mono<UserNoteResponse> findByUserNoteId(UUID userNoteId);

    @Override
    Flux<UserNoteResponse> findByUserId(UUID userId);

    @Override
    Flux<UserNoteResponse> findByNoteId(UUID noteId);

    @Override
    Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);

    @Override
    Mono<UserNoteResponse> replaceByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    Mono<UserNoteResponse> replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    Mono<UserNoteResponse> mergeByUserNoteId(UUID userNoteId, UserNoteRequest request);

    @Override
    Mono<UserNoteResponse> mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request);

    @Override
    Mono<Void> deleteByUserNoteId(UUID userNoteId);

    @Override
    Mono<Void> deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
