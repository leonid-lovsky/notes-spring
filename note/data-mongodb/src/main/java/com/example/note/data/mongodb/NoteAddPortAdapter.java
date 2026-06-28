package com.example.note.data.mongodb;

import com.example.note.domain.NoteAddPort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteAddPortAdapter implements NoteAddPort {

    private final MongoTemplate mongoTemplate;

    private final NoteMongoMapper noteMongoMapper;

    NoteAddPortAdapter(MongoTemplate mongoTemplate, NoteMongoMapper noteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteDocument document = this.noteMongoMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.noteMongoMapper.toResponse(document);
    }

}
