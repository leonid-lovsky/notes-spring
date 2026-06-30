package com.example.usernote.data.jpa.adapter;

import java.util.Objects;
import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceJpaAdapter implements UserNoteReplaceContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapperContract userNoteJpaMapper;

    UserNoteReplaceJpaAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteJpaMapperContract userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
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
