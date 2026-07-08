package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteMongoReactiveMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteMongoReactiveAdapter implements UserNoteContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper;

    UserNoteMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteMongoReactiveMapper = userNoteMongoReactiveMapper;
    }

    @Override
    public Mono<UserNoteResponse> add(UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.insert(this.userNoteMongoReactiveMapper.toNewDocument(request))
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Mono<UserNoteResponse> findById(UUID id) {
        return this.userNoteMongoReactiveRepository.findById(id).map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Flux<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteMongoReactiveRepository.findByNoteId(noteId)
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Flux<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteMongoReactiveRepository.findByUserId(userId)
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<Void> remove(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.deleteByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Mono<UserNoteResponse> replace(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.findByUserIdAndNoteId(userId, noteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)))
            .flatMap((existing) -> this.userNoteMongoReactiveRepository
                .save(this.userNoteMongoReactiveMapper.toExistingDocument(existing.getId(), request)))
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

}
