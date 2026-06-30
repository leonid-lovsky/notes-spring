package com.example.usernote.data.mongodb.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceMongoAdapter implements UserNoteReplaceContract {

    private final MongoTemplate mongoTemplate;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteReplaceMongoAdapter(MongoTemplate mongoTemplate, UserNoteMongoMapperContract userNoteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteRequest normalized = new UserNoteRequest(userId, noteId, request.role());
        UserNoteDocument document = this.mongoTemplate.save(this.userNoteMongoMapper.toDocument(normalized));
        return this.userNoteMongoMapper.toResponse(document);
    }

}
