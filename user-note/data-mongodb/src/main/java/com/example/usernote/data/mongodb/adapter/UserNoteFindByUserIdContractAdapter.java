package com.example.usernote.data.mongodb.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByUserIdContract;
import com.example.usernote.data.mongodb.mapper.UserNoteDocumentMapperContract;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdContractAdapter implements UserNoteFindByUserIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteDocumentMapperContract userNoteDocumentMapper;

    UserNoteFindByUserIdContractAdapter(UserNoteMongoRepository userNoteMongoRepository,
            UserNoteDocumentMapperContract userNoteDocumentMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteDocumentMapper = userNoteDocumentMapper;
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteMongoRepository.findByIdUserId(userId)
            .stream()
            .map(this.userNoteDocumentMapper::toResponse)
            .toList();
    }

}
