package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteReactiveDocumentMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdMongoReactiveAdapter implements UserNoteFindByUserIdContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper;

    UserNoteFindByUserIdMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteReactiveDocumentMapper = userNoteReactiveDocumentMapper;
    }

    @Override
    public Flux<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteMongoReactiveRepository.findByIdUserId(userId)
            .map(this.userNoteReactiveDocumentMapper::toResponse);
    }

}
