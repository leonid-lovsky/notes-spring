package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteFindByNoteIdPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
class UserNoteFindByNoteIdPortAdapter implements UserNoteFindByNoteIdPort {

    private final UserNoteMongoRepository userNoteMongoRepository;

    UserNoteFindByNoteIdPortAdapter(UserNoteMongoRepository userNoteMongoRepository) {
        this.userNoteMongoRepository = userNoteMongoRepository;
    }

    @Override
    public List<UserNote> findByNoteId(UUID noteId) {
        return userNoteMongoRepository.findByIdNoteId(noteId).stream()
                .map(d -> new UserNote(d.getId().getUserId(), d.getId().getNoteId(), d.getRole()))
                .toList();
    }
}
