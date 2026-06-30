package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteReplaceContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteMongoReactiveMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceMongoReactiveAdapter implements UserNoteReplaceContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper;

    UserNoteReplaceMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteMongoReactiveMapper = userNoteMongoReactiveMapper;
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
