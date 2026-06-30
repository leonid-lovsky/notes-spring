package com.example.usernote.data.mongodb.adapter;

import com.example.usernote.contract.UserNoteAddContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddMongoAdapter implements UserNoteAddContract {

    private final MongoTemplate mongoTemplate;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteAddMongoAdapter(MongoTemplate mongoTemplate, UserNoteMongoMapperContract userNoteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UserNoteDocument document = this.mongoTemplate.insert(this.userNoteMongoMapper.toDocument(request));
        return this.userNoteMongoMapper.toResponse(document);
    }

}
