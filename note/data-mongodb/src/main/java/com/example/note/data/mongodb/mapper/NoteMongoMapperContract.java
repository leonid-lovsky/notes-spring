package com.example.note.data.mongodb.mapper;

import java.util.UUID;

import com.example.note.data.mongodb.model.NoteDocument;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteMongoMapperContract {

    NoteDocument toNewDocument(NoteRequest request);

    NoteDocument toExistingDocument(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteDocument document);

}
