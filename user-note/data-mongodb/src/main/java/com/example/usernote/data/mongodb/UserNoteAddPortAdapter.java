package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteAddPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddPortAdapter implements UserNoteAddPort {

    private final MongoTemplate mongoTemplate;

    UserNoteAddPortAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UserNote add(UserNote userNote) {
        mongoTemplate.insert(new UserNoteDocument(
                new UserNoteKey(userNote.userId(), userNote.noteId()), userNote.role()));
        return userNote;
    }
}
