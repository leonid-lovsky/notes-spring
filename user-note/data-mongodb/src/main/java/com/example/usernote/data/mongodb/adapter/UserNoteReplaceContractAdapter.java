package com.example.usernote.data.mongodb.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.data.mongodb.mapper.UserNoteDocumentMapperContract;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceContractAdapter implements UserNoteReplaceContract {

    private final MongoTemplate mongoTemplate;

    private final UserNoteDocumentMapperContract userNoteDocumentMapper;

    UserNoteReplaceContractAdapter(MongoTemplate mongoTemplate, UserNoteDocumentMapperContract userNoteDocumentMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteDocumentMapper = userNoteDocumentMapper;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteRequest normalized = new UserNoteRequest(userId, noteId, request.role());
        UserNoteDocument document = this.mongoTemplate.save(this.userNoteDocumentMapper.toDocument(normalized));
        return this.userNoteDocumentMapper.toResponse(document);
    }

}
