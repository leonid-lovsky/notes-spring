package com.example.usernote.data.mongodb.adapter;

import java.util.UUID;

import com.example.usernote.contract.UserNoteRemoveContract;
import com.example.usernote.data.mongodb.document.UserNoteKey;
import com.example.usernote.data.mongodb.repository.UserNoteMongoRepository;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemoveContractAdapter implements UserNoteRemoveContract {

    private final UserNoteMongoRepository userNoteMongoRepository;

    UserNoteRemoveContractAdapter(UserNoteMongoRepository userNoteMongoRepository) {
        this.userNoteMongoRepository = userNoteMongoRepository;
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.userNoteMongoRepository.deleteById(new UserNoteKey(userId, noteId));
    }

}
