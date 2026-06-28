package com.example.usernote.data.mongodb;

import java.util.UUID;

import com.example.usernote.domain.UserNoteReplacePort;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplacePortAdapter implements UserNoteReplacePort {

    private final MongoTemplate mongoTemplate;

    private final UserNoteMongoMapper userNoteMongoMapper;

    UserNoteReplacePortAdapter(MongoTemplate mongoTemplate, UserNoteMongoMapper userNoteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteRequest normalized = new UserNoteRequest(userId, noteId, request.role());
        UserNoteDocument document = this.userNoteMongoMapper.toDocument(normalized);
        this.mongoTemplate.save(document);
        return this.userNoteMongoMapper.toResponse(document);
    }

}
