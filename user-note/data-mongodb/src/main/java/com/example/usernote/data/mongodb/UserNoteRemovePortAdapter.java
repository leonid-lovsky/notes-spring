package com.example.usernote.data.mongodb;

import java.util.UUID;

import com.example.usernote.domain.UserNoteRemovePort;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemovePortAdapter implements UserNoteRemovePort {

    private final UserNoteMongoRepository userNoteMongoRepository;

    UserNoteRemovePortAdapter(UserNoteMongoRepository userNoteMongoRepository) {
        this.userNoteMongoRepository = userNoteMongoRepository;
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        this.userNoteMongoRepository.deleteById(new UserNoteKey(userId, noteId));
    }

}
