package com.example.usernote.data.jpa.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteReplaceContract;
import com.example.usernote.data.jpa.mapper.UserNoteJpaMapperContract;
import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;
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
        UserNoteRequest normalized = new UserNoteRequest(userId, noteId, request.role());
        UserNoteEntity saved = this.userNoteJpaRepository.save(this.userNoteJpaMapper.toEntity(normalized));
        return this.userNoteJpaMapper.toResponse(saved);
    }

}
