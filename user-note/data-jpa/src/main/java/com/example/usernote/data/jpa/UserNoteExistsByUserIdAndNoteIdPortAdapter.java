package com.example.usernote.data.jpa;

import java.util.UUID;

import com.example.usernote.domain.UserNoteExistsByUserIdAndNoteIdPort;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteExistsByUserIdAndNoteIdPortAdapter implements UserNoteExistsByUserIdAndNoteIdPort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteExistsByUserIdAndNoteIdPortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.existsById(new UserNoteId(userId, noteId));
    }

}
