package com.example.usernote.data.mongodb.adapter;

import java.util.List;
import java.util.UUID;

import com.example.usernote.contract.UserNoteServiceInterface;
import com.example.usernote.data.mongodb.mapper.UserNoteMongoMapperContract;
import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;
import com.example.usernote.domain.NoteNotFoundException;
import com.example.usernote.domain.UserNotFoundException;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import com.example.usernote.domain.UserNoteRole;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteService implements UserNoteServiceInterface {

    private final MongoTemplate mongoTemplate;

    private final UserNoteMongoRepository userNoteMongoRepository;

    private final UserNoteMongoMapperContract userNoteMongoMapper;

    UserNoteService(MongoTemplate mongoTemplate, UserNoteMongoRepository userNoteMongoRepository,
            UserNoteMongoMapperContract userNoteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.userNoteMongoMapper = userNoteMongoMapper;
    }

    @Override
    public Boolean existsByUserNoteId(UUID userNoteId) {
        return this.userNoteMongoRepository.existsById(userNoteId);
    }

    @Override
    public Boolean existsByUserId(UUID userId) {
        return this.userNoteMongoRepository.existsByUserId(userId);
    }

    @Override
    public Boolean existsByNoteId(UUID noteId) {
        return this.userNoteMongoRepository.existsByNoteId(noteId);
    }

    @Override
    public Boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public UserNoteResponse create(UserNoteRequest request) {
        UserNoteDocument document = this.mongoTemplate.insert(this.userNoteMongoMapper.toNewDocument(request));
        return this.userNoteMongoMapper.toResponse(document);
    }

    @Override
    public UserNoteResponse findByUserNoteId(UUID userNoteId) {
        return this.userNoteMongoRepository.findById(userNoteId)
            .map(this.userNoteMongoMapper::toResponse)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        if (!existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        return this.userNoteMongoRepository.findByUserId(userId)
            .stream()
            .map(this.userNoteMongoMapper::toResponse)
            .toList();
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        if (!existsByNoteId(noteId)) {
            throw new NoteNotFoundException(noteId);
        }
        return this.userNoteMongoRepository.findByNoteId(noteId)
            .stream()
            .map(this.userNoteMongoMapper::toResponse)
            .toList();
    }

    @Override
    public UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteMongoMapper::toResponse)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
    }

    @Override
    public UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        if (!this.userNoteMongoRepository.existsById(userNoteId)) {
            throw new UserNoteNotFoundException(userNoteId);
        }
        UserNoteDocument saved = this.mongoTemplate
            .save(this.userNoteMongoMapper.toExistingDocument(userNoteId, request));
        return this.userNoteMongoMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteDocument existing = this.userNoteMongoRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        UserNoteDocument saved = this.mongoTemplate
            .save(this.userNoteMongoMapper.toExistingDocument(existing.getId(), request));
        return this.userNoteMongoMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        UserNoteDocument existing = this.userNoteMongoRepository.findById(userNoteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
        UserNoteRequest merged = merge(existing, request);
        UserNoteDocument saved = this.mongoTemplate
            .save(this.userNoteMongoMapper.toExistingDocument(userNoteId, merged));
        return this.userNoteMongoMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteDocument existing = this.userNoteMongoRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        UserNoteRequest merged = merge(existing, request);
        UserNoteDocument saved = this.mongoTemplate
            .save(this.userNoteMongoMapper.toExistingDocument(existing.getId(), merged));
        return this.userNoteMongoMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse deleteByUserNoteId(UUID userNoteId) {
        UserNoteDocument existing = this.userNoteMongoRepository.findById(userNoteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
        this.userNoteMongoRepository.deleteById(userNoteId);
        return this.userNoteMongoMapper.toResponse(existing);
    }

    @Override
    public UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId) {
        UserNoteDocument existing = this.userNoteMongoRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        this.userNoteMongoRepository.delete(existing);
        return this.userNoteMongoMapper.toResponse(existing);
    }

    private static UserNoteRequest merge(UserNoteDocument existing, UserNoteRequest request) {
        UUID userId = (request.userId() != null) ? request.userId() : existing.getUserId();
        UUID noteId = (request.noteId() != null) ? request.noteId() : existing.getNoteId();
        UserNoteRole role = (request.role() != null) ? request.role() : existing.getRole();
        return new UserNoteRequest(userId, noteId, role);
    }

}
