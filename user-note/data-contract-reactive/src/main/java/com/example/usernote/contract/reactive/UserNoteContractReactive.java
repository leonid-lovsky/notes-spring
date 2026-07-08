package com.example.usernote.contract.reactive;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserNoteContractReactive {

    Mono<UserNoteResponse> add(UserNoteRequest request);

    Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId);

    Mono<UserNoteResponse> findById(UUID id);

    Flux<UserNoteResponse> findByNoteId(UUID noteId);

    Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId);

    Flux<UserNoteResponse> findByUserId(UUID userId);

    Mono<Void> remove(UUID userId, UUID noteId);

    Mono<UserNoteResponse> replace(UUID userId, UUID noteId, UserNoteRequest request);

}
