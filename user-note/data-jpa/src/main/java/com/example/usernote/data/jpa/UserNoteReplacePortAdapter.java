package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteReplacePort;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserNoteReplacePortAdapter implements UserNoteReplacePort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapper userNoteJpaMapper;

    UserNoteReplacePortAdapter(UserNoteJpaRepository userNoteJpaRepository, UserNoteJpaMapper userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public UserNoteResponse replace(UUID userId, UUID noteId, UserNoteRequest request) {
        UserNoteRequest normalized = new UserNoteRequest(userId, noteId, request.role());
        UserNoteEntity saved = userNoteJpaRepository.save(userNoteJpaMapper.toEntity(normalized));
        return userNoteJpaMapper.toResponse(saved);
    }

}
