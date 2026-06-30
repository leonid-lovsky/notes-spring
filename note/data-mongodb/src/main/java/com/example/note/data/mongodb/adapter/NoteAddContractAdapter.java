package com.example.note.data.mongodb.adapter;

import com.example.note.contract.NoteAddContract;
import com.example.note.data.mongodb.mapper.NoteDocumentMapperContract;
import com.example.note.data.mongodb.model.NoteDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteAddContractAdapter implements NoteAddContract {

    private final MongoTemplate mongoTemplate;

    private final NoteDocumentMapperContract noteDocumentMapper;

    NoteAddContractAdapter(MongoTemplate mongoTemplate, NoteDocumentMapperContract noteDocumentMapper) {
        this.mongoTemplate = mongoTemplate;
        this.noteDocumentMapper = noteDocumentMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteDocument document = this.noteDocumentMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.noteDocumentMapper.toResponse(document);
    }

}
