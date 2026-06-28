package com.example.note.data.mongodb.mapper;

import java.util.UUID;

import com.example.note.data.mongodb.document.NoteDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Component;

@Component
class NoteDocumentMapper implements NoteDocumentMapperContract {

    @Override
    public NoteDocument toNewDocument(NoteRequest request) {
        return new NoteDocument(UUID.randomUUID(), request.content());
    }

    @Override
    public NoteDocument toExistingDocument(UUID id, NoteRequest request) {
        return new NoteDocument(id, request.content());
    }

    @Override
    public NoteResponse toResponse(NoteDocument document) {
        return new NoteResponse(document.getId(), document.getContent());
    }

}
