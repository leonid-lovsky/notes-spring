package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByUserIdPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
class UserNoteFindByUserIdPortAdapter implements UserNoteFindByUserIdPort {

    private final UserNoteMongoRepository userNoteMongoRepository;

    UserNoteFindByUserIdPortAdapter(UserNoteMongoRepository userNoteMongoRepository) {
        this.userNoteMongoRepository = userNoteMongoRepository;
    }

    @Override
    public List<UserNote> findByUserId(UUID userId) {
        return userNoteMongoRepository.findByIdUserId(userId).stream()
                .map(d -> new UserNote(d.getId().getUserId(), d.getId().getNoteId(), d.getRole()))
                .toList();
    }
}
