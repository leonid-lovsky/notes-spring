package com.example.usernote.data.mongodb.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteFindByNoteIdContract;
import com.example.usernote.data.mongodb.mapper.UserNoteDocumentMapperContract;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdContractAdapter implements UserNoteFindByNoteIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteDocumentMapperContract userNoteDocumentMapper;

    UserNoteFindByNoteIdContractAdapter(UserNoteMongoRepository userNoteMongoRepository,
            UserNoteDocumentMapperContract userNoteDocumentMapper) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteDocumentMapper = userNoteDocumentMapper;
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteMongoRepository.findByIdNoteId(noteId)
            .stream()
            .map(this.userNoteDocumentMapper::toResponse)
            .toList();
    }

}
