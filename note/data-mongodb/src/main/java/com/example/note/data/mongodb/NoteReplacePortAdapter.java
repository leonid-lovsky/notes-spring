package com.example.note.data.mongodb;

import com.example.note.domain.Note;
import com.example.note.domain.NoteReplacePort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteReplacePortAdapter implements NoteReplacePort {

    private final MongoTemplate mongoTemplate;

    NoteReplacePortAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void replace(Note note) {
        mongoTemplate.save(new NoteDocument(note.id(), note.content()));
    }
}
