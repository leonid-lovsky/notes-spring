package com.example.usernote.data.mongodb.mapper;

import java.util.UUID;

import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteMongoMapper implements UserNoteMongoMapperContract {

    @Override
    public UserNoteDocument toNewDocument(UserNoteRequest request) {
        return new UserNoteDocument(UUID.randomUUID(), request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteDocument toExistingDocument(UUID id, UserNoteRequest request) {
        return new UserNoteDocument(id, request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteDocument document) {
        return new UserNoteResponse(document.getId(), document.getUserId(), document.getNoteId(), document.getRole());
    }
}
