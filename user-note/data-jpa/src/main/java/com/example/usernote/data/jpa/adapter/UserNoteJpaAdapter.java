package com.example.usernote.data.jpa.adapter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.example.usernote.contract.UserNoteContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteJpaAdapter implements UserNoteContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteJpaAdapter(UserNoteJpaRepository userNoteJpaRepository, UserNoteJpaMapperContract userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public UserNoteResponse add(UserNoteRequest request) {
        UserNoteEntity saved = this.userNoteJpaRepository.save(this.userNoteJpaMapper.toNewEntity(request));
        return this.userNoteJpaMapper.toResponse(saved);
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.existsByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public Optional<UserNoteResponse> findById(UUID id) {
        return this.userNoteJpaRepository.findById(id).map(this.userNoteJpaMapper::toResponse);
    }

    @Override
    public List<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteJpaRepository.findByNoteId(noteId)
            .stream()
            .map(this.userNoteJpaMapper::toResponse)
            .toList();
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.findByUserIdAndNoteId(userId, noteId).map(this.userNoteJpaMapper::toResponse);
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteJpaRepository.findByUserId(userId)
            .stream()
            .map(this.userNoteJpaMapper::toResponse)
            .toList();
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.userNoteJpaRepository.deleteByUserIdAndNoteId(userId, noteId);
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteEntity existing = this.userNoteJpaRepository.findByUserIdAndNoteId(userId, noteId)
            .orElseThrow(() -> new UserNoteNotFoundException(userId, noteId));
        UserNoteEntity saved = this.userNoteJpaRepository
            .save(this.userNoteJpaMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), request));
        return this.userNoteJpaMapper.toResponse(saved);
    }

}
