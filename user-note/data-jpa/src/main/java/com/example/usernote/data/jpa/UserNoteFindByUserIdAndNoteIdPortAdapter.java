package com.example.usernote.data.jpa;

import java.util.Optional;
import java.util.UUID;

import com.example.usernote.domain.UserNoteFindByUserIdAndNoteIdPort;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdPortAdapter implements UserNoteFindByUserIdAndNoteIdPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapper userNoteJpaMapper;

    UserNoteFindByUserIdAndNoteIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository,
            UserNoteJpaMapper userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public Optional<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.findById(new UserNoteId(userId, noteId))
            .map(this.userNoteJpaMapper::toResponse);
    }

}
