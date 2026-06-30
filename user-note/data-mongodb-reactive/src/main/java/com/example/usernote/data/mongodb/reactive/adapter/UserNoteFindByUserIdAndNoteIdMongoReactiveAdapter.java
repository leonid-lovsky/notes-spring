package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdAndNoteIdContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteMongoReactiveMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdMongoReactiveAdapter implements UserNoteFindByUserIdAndNoteIdContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper;

    UserNoteFindByUserIdAndNoteIdMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteMongoReactiveMapper = userNoteMongoReactiveMapper;
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

}
