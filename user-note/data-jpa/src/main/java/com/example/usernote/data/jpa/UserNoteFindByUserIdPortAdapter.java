package com.example.usernote.data.jpa;

import java.util.List;
import java.util.UUID;

import com.example.usernote.domain.UserNoteFindByUserIdPort;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdPortAdapter implements UserNoteFindByUserIdPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    private final UserNoteJpaMapper userNoteJpaMapper;

    UserNoteFindByUserIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository, UserNoteJpaMapper userNoteJpaMapper) {
        this.userNoteJpaRepository = userNoteJpaRepository;
        this.userNoteJpaMapper = userNoteJpaMapper;
    }

    @Override
    public List<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteJpaRepository.findByIdUserId(userId)
            .stream()
            .map(this.userNoteJpaMapper::toResponse)
            .toList();
    }

}
