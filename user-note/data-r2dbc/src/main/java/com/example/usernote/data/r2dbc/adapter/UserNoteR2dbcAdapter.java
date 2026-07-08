package com.example.usernote.data.r2dbc.adapter;

import java.util.Objects;
import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteContractReactive;
import com.example.usernote.data.r2dbc.mapper.UserNoteR2dbcMapperContract;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteR2dbcAdapter implements UserNoteContractReactive {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    private final UserNoteR2dbcMapperContract userNoteR2dbcMapper;

    UserNoteR2dbcAdapter(UserNoteR2dbcRepository userNoteR2dbcRepository,
            UserNoteR2dbcMapperContract userNoteR2dbcMapper) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
        this.userNoteR2dbcMapper = userNoteR2dbcMapper;
    }

    @Override
    public Mono<UserNoteResponse> add(UserNoteRequest request) {
        return this.userNoteR2dbcRepository.save(this.userNoteR2dbcMapper.toNewEntity(request))
            .map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Mono<UserNoteResponse> findById(UUID id) {
        return this.userNoteR2dbcRepository.findById(id).map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Flux<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteR2dbcRepository.findByNoteId(noteId).map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Flux<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteR2dbcRepository.findByUserId(userId).map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<Void> remove(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.deleteByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Mono<UserNoteResponse> replace(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.userNoteR2dbcRepository.findByUserIdAndNoteId(userId, noteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)))
            .flatMap((existing) -> this.userNoteR2dbcRepository
                .save(this.userNoteR2dbcMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), request)))
            .map(this.userNoteR2dbcMapper::toResponse);
    }

}
