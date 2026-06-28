package com.example.note.data.mongodb;

import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

interface NoteMongoMapper {

    NoteDocument toNewDocument(NoteRequest request);

    NoteDocument toExistingDocument(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteDocument document);

}
