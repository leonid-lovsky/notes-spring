package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteReplaceContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteReactiveDocumentMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceMongoReactiveAdapter implements UserNoteReplaceContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper;

    UserNoteReplaceMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteReactiveDocumentMapperContract userNoteReactiveDocumentMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteReactiveDocumentMapper = userNoteReactiveDocumentMapper;
    }

    @Override
    public Mono<UserNoteResponse> replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteRequest normalized = new UserNoteRequest(userId, noteId, request.role());
        return this.userNoteMongoReactiveRepository.save(this.userNoteReactiveDocumentMapper.toDocument(normalized))
            .map(this.userNoteReactiveDocumentMapper::toResponse);
    }

}
