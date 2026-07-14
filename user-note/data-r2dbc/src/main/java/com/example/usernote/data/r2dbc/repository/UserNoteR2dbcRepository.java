package com.example.usernote.data.r2dbc.repository;

import java.util.UUID;

import com.example.usernote.data.r2dbc.model.UserNoteR2dbcEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoteR2dbcRepository extends ReactiveCrudRepository<UserNoteR2dbcEntity, UUID> {

    Flux<UserNoteR2dbcEntity> findByUserId(UUID userId);

    Flux<UserNoteR2dbcEntity> findByNoteId(UUID noteId);

    Mono<UserNoteR2dbcEntity> findByUserIdAndNoteId(UUID userId, UUID noteId);

    Mono<Boolean> existsByUserId(UUID userId);

    Mono<Boolean> existsByNoteId(UUID noteId);

    Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId);

    Mono<Void> deleteByUserIdAndNoteId(UUID userId, UUID noteId);
}
