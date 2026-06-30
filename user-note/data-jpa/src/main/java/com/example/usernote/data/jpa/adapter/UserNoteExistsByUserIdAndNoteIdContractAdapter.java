package com.example.usernote.data.jpa.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteExistsByUserIdAndNoteIdContract;
import com.example.usernote.data.jpa.model.UserNoteId;
import com.example.usernote.data.jpa.repository.UserNoteJpaRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteExistsByUserIdAndNoteIdContractAdapter implements UserNoteExistsByUserIdAndNoteIdContract {

    private final UserNoteJpaRepository userNoteJpaRepository;

    UserNoteExistsByUserIdAndNoteIdContractAdapter(UserNoteJpaRepository userNoteJpaRepository) {
        this.userNoteJpaRepository = userNoteJpaRepository;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteJpaRepository.existsById(new UserNoteId(userId, noteId));
    }

}
