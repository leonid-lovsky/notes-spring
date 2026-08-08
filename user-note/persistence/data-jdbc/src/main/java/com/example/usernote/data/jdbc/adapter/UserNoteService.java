package com.example.usernote.data.jdbc.adapter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.example.usernote.contract.UserNoteServiceInterface;
import com.example.usernote.data.jdbc.mapper.UserNoteJdbcMapperContract;
import com.example.usernote.data.jdbc.model.UserNoteJdbcEntity;
import com.example.usernote.data.jdbc.repository.UserNoteJdbcRepository;
import com.example.usernote.domain.NoteNotFoundException;
import com.example.usernote.domain.UserNotFoundException;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import com.example.usernote.domain.UserNoteRole;

import org.springframework.stereotype.Service;

@Service
class UserNoteService implements UserNoteServiceInterface {

    private final UserNoteJdbcRepository userNoteJdbcRepository;

    private final UserNoteJdbcMapperContract userNoteJdbcMapper;

    UserNoteService(UserNoteJdbcRepository userNoteJdbcRepository, UserNoteJdbcMapperContract userNoteJdbcMapper) {
        this.userNoteJdbcRepository = userNoteJdbcRepository;
        this.userNoteJdbcMapper = userNoteJdbcMapper;
    }

    @Override
    public Boolean existsByUserNoteId(UUID userNoteId) {
        return this.userNoteJdbcRepository.existsById(userNoteId);
    }

    @Override
    public Boolean existsByUserId(UUID userId) {
        return this.userNoteJdbcRepository.existsByUserId(userId);
    }

    @Override
    public Boolean existsByNoteId(UUID noteId) {
        return this.userNoteJdbcRepository.existsByNoteId(noteId);
    }

    @Override
    public Boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJdbcRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public UserNoteResponse create(UserNoteRequest request) {
        UserNoteJdbcEntity saved = this.userNoteJdbcRepository.save(this.userNoteJdbcMapper.toNewEntity(request));
        return this.userNoteJdbcMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse findByUserNoteId(UUID userNoteId) {
        return this.userNoteJdbcRepository.findById(userNoteId)
            .map(this.userNoteJdbcMapper::toResponse)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        if (!existsByUserId(userId)) {
            throw new UserNotFoundException(userId);
        }
        return this.userNoteJdbcRepository.findByUserId(userId)
            .stream()
            .map(this.userNoteJdbcMapper::toResponse)
            .toList();
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        if (!existsByNoteId(noteId)) {
            throw new NoteNotFoundException(noteId);
        }
        return this.userNoteJdbcRepository.findByNoteId(noteId)
            .stream()
            .map(this.userNoteJdbcMapper::toResponse)
            .toList();
    }

    @Override
    public UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJdbcRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteJdbcMapper::toResponse)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
    }

    @Override
    public UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        if (!this.userNoteJdbcRepository.existsById(userNoteId)) {
            throw new UserNoteNotFoundException(userNoteId);
        }
        UserNoteJdbcEntity saved = this.userNoteJdbcRepository
            .save(this.userNoteJdbcMapper.toExistingEntity(userNoteId, request));
        return this.userNoteJdbcMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteJdbcEntity existing = this.userNoteJdbcRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        UserNoteJdbcEntity saved = this.userNoteJdbcRepository
            .save(this.userNoteJdbcMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), request));
        return this.userNoteJdbcMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        UserNoteJdbcEntity existing = this.userNoteJdbcRepository.findById(userNoteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
        UserNoteRequest merged = merge(existing, request);
        UserNoteJdbcEntity saved = this.userNoteJdbcRepository
            .save(this.userNoteJdbcMapper.toExistingEntity(userNoteId, merged));
        return this.userNoteJdbcMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteJdbcEntity existing = this.userNoteJdbcRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        UserNoteRequest merged = merge(existing, request);
        UserNoteJdbcEntity saved = this.userNoteJdbcRepository
            .save(this.userNoteJdbcMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), merged));
        return this.userNoteJdbcMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse deleteByUserNoteId(UUID userNoteId) {
        UserNoteJdbcEntity existing = this.userNoteJdbcRepository.findById(userNoteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
        this.userNoteJdbcRepository.deleteById(userNoteId);
        return this.userNoteJdbcMapper.toResponse(existing);
    }

    @Override
    public UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId) {
        UserNoteJdbcEntity existing = this.userNoteJdbcRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        this.userNoteJdbcRepository.delete(existing);
        return this.userNoteJdbcMapper.toResponse(existing);
    }

    private static UserNoteRequest merge(UserNoteJdbcEntity existing, UserNoteRequest request) {
        UUID userId = (request.userId() != null) ? request.userId() : existing.getUserId();
        UUID noteId = (request.noteId() != null) ? request.noteId() : existing.getNoteId();
        UserNoteRole role = (request.role() != null) ? request.role() : existing.getRole();
        return new UserNoteRequest(userId, noteId, role);
    }
}
