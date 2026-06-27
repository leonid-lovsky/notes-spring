package com.example.note.data.mongodb;

import com.example.note.domain.Note;
import com.example.note.domain.NoteAddPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class NoteAddPortAdapter implements NoteAddPort {

    private final MongoTemplate mongoTemplate;

    NoteAddPortAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Note add(Note note) {
        UUID id = UUID.randomUUID();
        mongoTemplate.insert(new NoteDocument(id, note.content()));
        return new Note(id, note.content());
    }
}
