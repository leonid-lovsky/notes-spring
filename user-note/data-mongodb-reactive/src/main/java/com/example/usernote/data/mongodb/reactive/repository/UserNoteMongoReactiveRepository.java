package com.example.usernote.data.mongodb.reactive.repository;

import java.util.UUID;

import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserNoteMongoReactiveRepository extends ReactiveMongoRepository<UserNoteReactiveDocument, UUID> {

    Flux<UserNoteReactiveDocument> findByUserId(UUID userId);

    Flux<UserNoteReactiveDocument> findByNoteId(UUID noteId);

    Mono<UserNoteReactiveDocument> findByUserIdAndNoteId(UUID userId, UUID noteId);

    Mono<Boolean> existsByUserId(UUID userId);

    Mono<Boolean> existsByNoteId(UUID noteId);

    Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId);

    Mono<Void> deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
