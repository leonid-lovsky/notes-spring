package com.example.usernote.data.jpa;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRemovePort;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemovePortAdapter implements UserNoteRemovePort {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteRemovePortAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.userNoteJpaRepository.deleteById(new UserNoteId(userId, noteId));
    }

}
