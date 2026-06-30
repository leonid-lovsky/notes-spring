package com.example.usernote.data.jpa.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.data.jpa.mapper.UserNoteEntityMapperContract;
import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceContractAdapter implements UserNoteReplaceContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteEntityMapperContract userNoteEntityMapper;

    UserNoteReplaceContractAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteEntityMapperContract userNoteEntityMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteEntityMapper = userNoteEntityMapper;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteRequest normalized = new UserNoteRequest(userId, noteId, request.role());
        UserNoteEntity saved = this.userNoteJpaRepository.save(this.userNoteEntityMapper.toEntity(normalized));
        return this.userNoteEntityMapper.toResponse(saved);
    }

}
