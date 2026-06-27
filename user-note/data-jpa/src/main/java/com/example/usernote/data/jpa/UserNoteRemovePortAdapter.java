package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteRemovePort;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class UserNoteRemovePortAdapter implements UserNoteRemovePort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteRemovePortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        userNoteJpaRepository.deleteById(new UserNoteId(userId, noteId));
    }
}
