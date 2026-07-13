package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteServiceInterfaceReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteMongoReactiveMapperContract;
import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveDocument;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.NoteNotFoundException;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import com.example.usernote.domain.UserNoteRole;
import com.example.usernote.domain.UserNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteService implements UserNoteServiceInterfaceReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper;

    UserNoteService(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteMongoReactiveMapper = userNoteMongoReactiveMapper;
    }

    @Override
    public Mono<Boolean> existsByUserNoteId(UUID userNoteId) {
        return this.userNoteMongoReactiveRepository.existsById(userNoteId);
    }

    @Override
    public Mono<Boolean> existsByUserId(UUID userId) {
        return this.userNoteMongoReactiveRepository.existsByUserId(userId);
    }

    @Override
    public Mono<Boolean> existsByNoteId(UUID noteId) {
        return this.userNoteMongoReactiveRepository.existsByNoteId(noteId);
    }

    @Override
    public Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Mono<UserNoteResponse> add(UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.insert(this.userNoteMongoReactiveMapper.toNewDocument(request))
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> findByUserNoteId(UUID userNoteId) {
        return this.userNoteMongoReactiveRepository.findById(userNoteId)
            .map(this.userNoteMongoReactiveMapper::toResponse)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userNoteId)));
    }

    @Override
    public Flux<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteMongoReactiveRepository.existsByUserId(userId)
            .flatMapMany((exists) -> exists
                    ? this.userNoteMongoReactiveRepository.findByUserId(userId)
                        .map(this.userNoteMongoReactiveMapper::toResponse)
                    : Flux.error(new UserNotFoundException(userId)));
    }

    @Override
    public Flux<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteMongoReactiveRepository.existsByNoteId(noteId)
            .flatMapMany((exists) -> exists
                    ? this.userNoteMongoReactiveRepository.findByNoteId(noteId)
                        .map(this.userNoteMongoReactiveMapper::toResponse)
                    : Flux.error(new NoteNotFoundException(noteId)));
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteMongoReactiveMapper::toResponse)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

    @Override
    public Mono<UserNoteResponse> replaceByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.existsById(userNoteId).flatMap((exists) -> exists
                ? this.userNoteMongoReactiveRepository
                    .save(this.userNoteMongoReactiveMapper.toExistingDocument(userNoteId, request))
                    .map(this.userNoteMongoReactiveMapper::toResponse)
                : Mono.error(new UserNoteNotFoundException(userNoteId)));
    }

    @Override
    public Mono<UserNoteResponse> replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.findByUserIdAndNoteId(userId, noteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)))
            .flatMap((existing) -> this.userNoteMongoReactiveRepository
                .save(this.userNoteMongoReactiveMapper.toExistingDocument(existing.getId(), request)))
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> mergeByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.findById(userNoteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userNoteId)))
            .flatMap((existing) -> {
                UserNoteRequest merged = merge(existing, request);
                return this.userNoteMongoReactiveRepository
                    .save(this.userNoteMongoReactiveMapper.toExistingDocument(userNoteId, merged));
            })
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.findByUserIdAndNoteId(userId, noteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)))
            .flatMap((existing) -> {
                UserNoteRequest merged = merge(existing, request);
                return this.userNoteMongoReactiveRepository
                    .save(this.userNoteMongoReactiveMapper.toExistingDocument(existing.getId(), merged));
            })
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<Void> deleteByUserNoteId(UUID userNoteId) {
        return this.userNoteMongoReactiveRepository.existsById(userNoteId)
            .flatMap((exists) -> exists ? this.userNoteMongoReactiveRepository.deleteById(userNoteId)
                    : Mono.error(new UserNoteNotFoundException(userNoteId)));
    }

    @Override
    public Mono<Void> deleteByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.existsByUserIdAndNoteId(userId, noteId)
            .flatMap((exists) -> exists
                    ? this.userNoteMongoReactiveRepository.deleteByUserIdAndNoteId(userId, noteId)
                    : Mono.error(new UserNoteNotFoundException(userId, noteId)));
    }

    private static UserNoteRequest merge(UserNoteReactiveDocument existing, UserNoteRequest request) {
        UUID userId = (request.userId() != null) ? request.userId() : existing.getUserId();
        UUID noteId = (request.noteId() != null) ? request.noteId() : existing.getNoteId();
        UserNoteRole role = (request.role() != null) ? request.role() : existing.getRole();
        return new UserNoteRequest(userId, noteId, role);
    }

}
