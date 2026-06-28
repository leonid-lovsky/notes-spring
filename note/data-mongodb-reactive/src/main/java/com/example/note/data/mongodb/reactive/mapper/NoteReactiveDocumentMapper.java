package com.example.note.data.mongodb.reactive.mapper;

import java.util.UUID;

import com.example.note.data.mongodb.reactive.document.NoteReactiveDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Component;

@Component
class NoteReactiveDocumentMapper implements NoteReactiveDocumentMapperContract {

    @Override
    public NoteReactiveDocument toNewDocument(NoteRequest request) {
        return new NoteReactiveDocument(UUID.randomUUID(), request.content());
    }

    @Override
    public NoteReactiveDocument toExistingDocument(UUID id, NoteRequest request) {
        return new NoteReactiveDocument(id, request.content());
    }

    @Override
    public NoteResponse toResponse(NoteReactiveDocument document) {
        return new NoteResponse(document.getId(), document.getContent());
    }

}
