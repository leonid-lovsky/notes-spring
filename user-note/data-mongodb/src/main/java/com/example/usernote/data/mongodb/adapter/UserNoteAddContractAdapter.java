package com.example.usernote.data.mongodb.adapter;

import com.example.usernote.contract.UserNoteAddContract;
import com.example.usernote.data.mongodb.document.UserNoteDocument;
import com.example.usernote.data.mongodb.mapper.UserNoteDocumentMapperContract;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddContractAdapter implements UserNoteAddContract {

    private final MongoTemplate mongoTemplate;

    private final UserNoteDocumentMapperContract userNoteDocumentMapper;

    UserNoteAddContractAdapter(MongoTemplate mongoTemplate, UserNoteDocumentMapperContract userNoteDocumentMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteDocumentMapper = userNoteDocumentMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UserNoteDocument document = this.mongoTemplate.insert(this.userNoteDocumentMapper.toDocument(request));
        return this.userNoteDocumentMapper.toResponse(document);
    }

}
