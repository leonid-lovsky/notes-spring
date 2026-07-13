package com.example.usernote.data.jpa.adapter;

import com.example.usernote.contract.UserNoteService;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
class UserNoteJpaAdapter implements UserNoteService {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteJpaAdapter(UserNoteJpaRepository userNoteJpaRepository, UserNoteJpaMapperContract userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public boolean existsByUserNoteId(UUID userNoteId) {
        return this.userNoteJpaRepository.existsById(userNoteId);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return this.userNoteJpaRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByNoteId(UUID noteId) {
        return this.userNoteJpaRepository.existsByNoteId(noteId);
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public UserNoteResponse create(UserNoteRequest request) {
        UserNoteEntity saved = this.userNoteJpaRepository.save(this.userNoteJpaMapper.toNewEntity(request));
        return this.userNoteJpaMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse findByUserNoteId(UUID userNoteId) {
        return this.userNoteJpaRepository.findById(userNoteId)
            .map(this.userNoteJpaMapper::toResponse)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteJpaRepository.findByUserId(userId)
            .stream()
            .map(this.userNoteJpaMapper::toResponse)
            .toList();
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteJpaRepository.findByNoteId(noteId)
            .stream()
            .map(this.userNoteJpaMapper::toResponse)
            .toList();
    }

    @Override
    public UserNoteResponse findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteJpaMapper::toResponse)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
    }

    @Override
    public UserNoteResponse replaceByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        if (!this.userNoteJpaRepository.existsById(userNoteId)) {
            throw new UserNoteNotFoundException(userNoteId);
        }
        UserNoteEntity saved = this.userNoteJpaRepository
            .save(this.userNoteJpaMapper.toExistingEntity(userNoteId, request));
        return this.userNoteJpaMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse replaceByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteEntity existing = this.userNoteJpaRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        UserNoteEntity saved = this.userNoteJpaRepository
            .save(this.userNoteJpaMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), request));
        return this.userNoteJpaMapper.toResponse(saved);
    }

    @Override
    public UserNoteResponse mergeByUserNoteId(UUID userNoteId, UserNoteRequest request) {
        // UserNoteRequest has no optional fields, so a partial merge is identical to a
        // full replace.
        return replaceByUserNoteId(userNoteId, request);
    }

    @Override
    public UserNoteResponse mergeByUserIdAndNoteId(UUID userId, UUID noteId, UserNoteRequest request) {
        return replaceByUserIdAndNoteId(userId, noteId, request);
    }

    @Override
    public UserNoteResponse deleteByUserNoteId(UUID userNoteId) {
        UserNoteEntity existing = this.userNoteJpaRepository.findById(userNoteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userNoteId));
        this.userNoteJpaRepository.deleteById(userNoteId);
        return this.userNoteJpaMapper.toResponse(existing);
    }

    @Override
    public UserNoteResponse deleteByUserIdAndNoteId(UUID userId, UUID noteId) {
        UserNoteEntity existing = this.userNoteJpaRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        this.userNoteJpaRepository.delete(existing);
        return this.userNoteJpaMapper.toResponse(existing);
    }

}
