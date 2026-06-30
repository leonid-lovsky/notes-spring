package com.example.usernote.data.mongodb.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdAndNoteIdContract;
import com.example.usernote.data.mongodb.mapper.UserNoteDocumentMapperContract;
import com.example.usernote.data.mongodb.model.UserNoteKey;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdContractAdapter implements UserNoteFindByUserIdAndNoteIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteDocumentMapperContract userNoteDocumentMapper;

    UserNoteFindByUserIdAndNoteIdContractAdapter(UserNoteMongoRepository userNoteMongoRepository,
            UserNoteDocumentMapperContract userNoteDocumentMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteDocumentMapper = userNoteDocumentMapper;
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.findById(new UserNoteKey(userId, noteId))
            .map(this.userNoteDocumentMapper::toResponse);
    }

}
