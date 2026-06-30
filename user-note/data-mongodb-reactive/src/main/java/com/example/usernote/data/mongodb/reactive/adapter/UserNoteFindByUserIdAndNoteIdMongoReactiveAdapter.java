package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdAndNoteIdContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteReactiveDocumentMapperContract;
import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveKey;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdMongoReactiveAdapter implements UserNoteFindByUserIdAndNoteIdContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper;

    UserNoteFindByUserIdAndNoteIdMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteReactiveDocumentMapper = userNoteReactiveDocumentMapper;
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.findById(new UserNoteReactiveKey(userId, noteId))
            .map(this.userNoteReactiveDocumentMapper::toResponse);
    }

}
