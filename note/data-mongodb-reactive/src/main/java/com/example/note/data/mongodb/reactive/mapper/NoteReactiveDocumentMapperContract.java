package com.example.note.data.mongodb.reactive.mapper;

import java.util.UUID;

import com.example.note.data.mongodb.reactive.document.NoteReactiveDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteReactiveDocumentMapperContract {

    NoteReactiveDocument toNewDocument(NoteRequest request);

    NoteReactiveDocument toExistingDocument(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteReactiveDocument document);

}
