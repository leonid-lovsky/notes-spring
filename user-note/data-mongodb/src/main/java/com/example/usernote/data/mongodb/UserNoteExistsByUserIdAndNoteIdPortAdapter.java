package com.example.usernote.data.mongodb;

import java.util.UUID;

import com.example.usernote.domain.UserNoteExistsByUserIdAndNoteIdPort;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteExistsByUserIdAndNoteIdPortAdapter implements UserNoteExistsByUserIdAndNoteIdPort {

    private final UserNoteMongoRepository userNoteMongoRepository;

    UserNoteExistsByUserIdAndNoteIdPortAdapter(UserNoteMongoRepository userNoteMongoRepository) {
        this.userNoteMongoRepository = userNoteMongoRepository;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.existsById(new UserNoteKey(userId, noteId));
    }

}
