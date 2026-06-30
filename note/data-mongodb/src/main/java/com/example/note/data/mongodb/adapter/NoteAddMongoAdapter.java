package com.example.note.data.mongodb.adapter;

import com.example.note.contract.NoteAddContract;
import com.example.note.data.mongodb.mapper.NoteMongoMapperContract;
import com.example.note.data.mongodb.model.NoteDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteAddMongoAdapter implements NoteAddContract {

    private final MongoTemplate mongoTemplate;

    private final NoteMongoMapperContract noteMongoMapper;

    NoteAddMongoAdapter(MongoTemplate mongoTemplate, NoteMongoMapperContract noteMongoMapper) {
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
