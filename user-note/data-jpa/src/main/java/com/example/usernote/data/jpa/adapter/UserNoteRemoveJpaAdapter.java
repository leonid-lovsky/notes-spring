package com.example.usernote.data.jpa.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteRemoveContract;
import com.example.usernote.data.jpa.model.UserNoteId;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemoveJpaAdapter implements UserNoteRemoveContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteRemoveJpaAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.userNoteJpaRepository.deleteById(new UserNoteId(userId, noteId));
    }

}
