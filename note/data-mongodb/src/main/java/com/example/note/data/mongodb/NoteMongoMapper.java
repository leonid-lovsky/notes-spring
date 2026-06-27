package com.example.note.data.mongodb;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import java.util.UUID;

interface NoteMongoMapper {

    NoteDocument toNewDocument(NoteRequest request);

    NoteDocument toExistingDocument(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteDocument document);

}
