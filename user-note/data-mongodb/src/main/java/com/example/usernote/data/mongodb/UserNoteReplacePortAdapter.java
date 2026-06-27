package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteReplacePort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplacePortAdapter implements UserNoteReplacePort {

    private final MongoTemplate mongoTemplate;

    UserNoteReplacePortAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void replace(UserNote userNote) {
        mongoTemplate.save(new UserNoteDocument(
                new UserNoteKey(userNote.userId(), userNote.noteId()), userNote.role()));
    }
}
