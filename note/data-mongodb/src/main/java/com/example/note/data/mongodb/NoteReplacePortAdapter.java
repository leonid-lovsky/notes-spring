package com.example.note.data.mongodb;

import java.util.UUID;

import com.example.note.domain.NoteReplacePort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteReplacePortAdapter implements NoteReplacePort {

    private final MongoTemplate mongoTemplate;

    private final NoteMongoMapper noteMongoMapper;

    NoteReplacePortAdapter(MongoTemplate mongoTemplate, NoteMongoMapper noteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        NoteDocument document = this.noteMongoMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.noteMongoMapper.toResponse(document);
    }

}
