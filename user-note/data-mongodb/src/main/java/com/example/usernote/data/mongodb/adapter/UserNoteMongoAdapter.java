package com.example.usernote.data.mongodb.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteContract;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteMongoAdapter implements UserNoteContract {

    private final MongoTemplate mongoTemplate;

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteMongoAdapter(MongoTemplate mongoTemplate, UserNoteMongoRepository userNoteMongoRepository,
            UserNoteMongoMapperContract userNoteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UserNoteDocument document = this.mongoTemplate.insert(this.userNoteMongoMapper.toNewDocument(request));
        return this.userNoteMongoMapper.toResponse(document);
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Optional<UserNoteResponse> findById(UUID id) {
        return this.userNoteMongoRepository.findById(id).map(this.userNoteMongoMapper::toResponse);
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteMongoRepository.findByNoteId(noteId)
            .stream()
            .map(this.userNoteMongoMapper::toResponse)
            .toList();
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteMongoMapper::toResponse);
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteMongoRepository.findByUserId(userId)
            .stream()
            .map(this.userNoteMongoMapper::toResponse)
            .toList();
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.userNoteMongoRepository.deleteByUserIdAndNoteId(userId, noteId);
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
