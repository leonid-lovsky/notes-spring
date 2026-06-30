package com.example.usernote.data.mongodb.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteExistsByUserIdAndNoteIdContract;
import com.example.usernote.data.mongodb.model.UserNoteKey;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteExistsByUserIdAndNoteIdMongoAdapter implements UserNoteExistsByUserIdAndNoteIdContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    UserNoteExistsByUserIdAndNoteIdMongoAdapter(UserNoteMongoRepository userNoteMongoRepository) {
        this.userNoteMongoRepository = userNoteMongoRepository;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoRepository.existsById(new UserNoteKey(userId, noteId));
    }

}
