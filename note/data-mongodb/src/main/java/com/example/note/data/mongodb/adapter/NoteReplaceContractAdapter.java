package com.example.note.data.mongodb.adapter;

import java.util.UUID;

import com.example.note.contract.NoteReplaceContract;
import com.example.note.data.mongodb.mapper.NoteDocumentMapperContract;
import com.example.note.data.mongodb.model.NoteDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteReplaceContractAdapter implements NoteReplaceContract {

    private final MongoTemplate mongoTemplate;

    private final NoteDocumentMapperContract noteDocumentMapper;

    NoteReplaceContractAdapter(MongoTemplate mongoTemplate, NoteDocumentMapperContract noteDocumentMapper) {
        this.mongoTemplate = mongoTemplate;
        this.noteDocumentMapper = noteDocumentMapper;
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        NoteDocument document = this.noteDocumentMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.noteDocumentMapper.toResponse(document);
    }

}
