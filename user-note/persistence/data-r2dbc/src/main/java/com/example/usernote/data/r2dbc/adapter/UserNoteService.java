package com.example.usernote.data.r2dbc.adapter;

import java.util.Objects;
import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteServiceReactiveInterface;
import com.example.usernote.data.r2dbc.mapper.UserNoteR2dbcMapperContract;
import com.example.usernote.data.r2dbc.model.UserNoteR2dbcEntity;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import com.example.usernote.domain.NoteNotFoundException;
import com.example.usernote.domain.UserNotFoundException;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import com.example.usernote.domain.UserNoteRole;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

@Service
class UserNoteService implements UserNoteServiceReactiveInterface {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    private final UserNoteR2dbcMapperContract userNoteR2dbcMapper;

    UserNoteService(UserNoteR2dbcRepository userNoteR2dbcRepository, UserNoteR2dbcMapperContract userNoteR2dbcMapper) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
        this.userNoteR2dbcMapper = userNoteR2dbcMapper;
    }

    @Override
    public Mono<Boolean> existsByUserNoteId(UUID userNoteId) {
        return this.userNoteR2dbcRepository.existsById(userNoteId);
    }

    @Override
    public Mono<Boolean> existsByUserId(UUID userId) {
        return this.userNoteR2dbcRepository.existsByUserId(userId);
    }

    @Override
    public Mono<Boolean> existsByNoteId(UUID noteId) {
        return this.userNoteR2dbcRepository.existsByNoteId(noteId);
    }

    @Override
    public Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Mono<UserNoteResponse> add(UserNoteRequest request) {
        return this.userNoteR2dbcRepository.save(this.userNoteR2dbcMapper.toNewEntity(request))
            .map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> findByUserNoteId(UUID userNoteId) {
        return this.userNoteR2dbcRepository.findById(userNoteId)
            .map(this.userNoteR2dbcMapper::toResponse)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userNoteId)));
    }

    @Override
    public Flux<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteR2dbcRepository.existsByUserId(userId).flatMapMany((exists) -> exists
            ? this.userNoteR2dbcRepository.findByUserId(userId).map(this.userNoteR2dbcMapper::toResponse)
            : Flux.error(new UserNotFoundException(userId)));
    }

    @Override
    public Flux<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteR2dbcRepository.existsByNoteId(noteId).flatMapMany((exists) -> exists
            ? this.userNoteR2dbcRepository.findByNoteId(noteId).map(this.userNoteR2dbcMapper::toResponse)
            : Flux.error(new NoteNotFoundException(noteId)));
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteR2dbcMapper::toResponse)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

    @Override
    public Mono<UserNoteResponse> replaceByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        return this.userNoteR2dbcRepository.existsById(userNoteId)
            .flatMap((exists) -> exists
                ? this.userNoteR2dbcRepository.save(this.userNoteR2dbcMapper.toExistingEntity(userNoteId, request))
                .map(this.userNoteR2dbcMapper::toResponse)
                : Mono.error(new UserNoteNotFoundException(userNoteId)));
    }

    @Override
    public Mono<UserNoteResponse> replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.userNoteR2dbcRepository.findByUserIdAndNoteId(userId, noteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)))
            .flatMap((existing) -> this.userNoteR2dbcRepository
                .save(this.userNoteR2dbcMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), request)))
            .map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> mergeByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        return this.userNoteR2dbcRepository.findById(userNoteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userNoteId)))
            .flatMap((existing) -> {
                UserNoteRequest merged = merge(existing, request);
                return this.userNoteR2dbcRepository.save(this.userNoteR2dbcMapper.toExistingEntity(userNoteId, merged));
            })
            .map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.userNoteR2dbcRepository.findByUserIdAndNoteId(userId, noteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)))
            .flatMap((existing) -> {
                UserNoteRequest merged = merge(existing, request);
                return this.userNoteR2dbcRepository
                    .save(this.userNoteR2dbcMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), merged));
            })
            .map(this.userNoteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteByUserNoteId(UUID userNoteId) {
        return this.userNoteR2dbcRepository.existsById(userNoteId)
            .flatMap((exists) -> exists ? this.userNoteR2dbcRepository.deleteById(userNoteId)
                : Mono.error(new UserNoteNotFoundException(userNoteId)));
    }

    @Override
    public Mono<Void> deleteByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.existsByUserIdAndNoteId(userId, noteId)
            .flatMap((exists) -> exists ? this.userNoteR2dbcRepository.deleteByUserIdAndNoteId(userId, noteId)
                : Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

    private static UserNoteRequest merge(UserNoteR2dbcEntity existing, UserNoteRequest request) {
        UUID userId = (request.userId() != null) ? request.userId() : existing.getUserId();
        UUID noteId = (request.noteId() != null) ? request.noteId() : existing.getNoteId();
        UserNoteRole role = (request.role() != null) ? request.role() : existing.getRole();
        return new UserNoteRequest(userId, noteId, role);
    }
}
