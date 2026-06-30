package com.example.note.data.mongodb.adapter;

import java.util.UUID;

import com.example.note.contract.NoteReplaceContract;
import com.example.note.data.mongodb.mapper.NoteMongoMapperContract;
import com.example.note.data.mongodb.model.NoteDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteReplaceMongoAdapter implements NoteReplaceContract {

    private final MongoTemplate mongoTemplate;

    private final NoteMongoMapperContract noteMongoMapper;

    NoteReplaceMongoAdapter(MongoTemplate mongoTemplate, NoteMongoMapperContract noteMongoMapper) {
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
