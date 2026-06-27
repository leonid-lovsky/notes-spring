package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByUserIdAndNoteIdPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdAndNoteIdPortAdapter implements UserNoteFindByUserIdAndNoteIdPort {

    private final UserNoteMongoRepository userNoteMongoRepository;

    UserNoteFindByUserIdAndNoteIdPortAdapter(UserNoteMongoRepository userNoteMongoRepository) {
        this.userNoteMongoRepository = userNoteMongoRepository;
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteMongoRepository.findById(new UserNoteKey(userId, noteId))
                .map(d -> new UserNote(d.getId().getUserId(), d.getId().getNoteId(), d.getRole()));
    }
}
