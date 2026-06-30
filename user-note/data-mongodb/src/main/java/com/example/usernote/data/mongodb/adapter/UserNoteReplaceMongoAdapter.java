package com.example.usernote.data.mongodb.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceMongoAdapter implements UserNoteReplaceContract {

    private final MongoTemplate mongoTemplate;

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteReplaceMongoAdapter(MongoTemplate mongoTemplate, UserNoteMongoRepository userNoteMongoRepository,
            UserNoteMongoMapperContract userNoteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteDocument existing = this.userNoteMongoRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        UserNoteDocument saved = this.mongoTemplate
            .save(this.userNoteMongoMapper.toExistingDocument(existing.getId(), request));
        return this.userNoteMongoMapper.toResponse(saved);
    }

}
