package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByNoteIdContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteReactiveDocumentMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdMongoReactiveAdapter implements UserNoteFindByNoteIdContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper;

    UserNoteFindByNoteIdMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteReactiveDocumentMapper = userNoteReactiveDocumentMapper;
    }

    @Override
    public Flux<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteMongoReactiveRepository.findByIdNoteId(noteId)
            .map(this.userNoteReactiveDocumentMapper::toResponse);
    }

}
