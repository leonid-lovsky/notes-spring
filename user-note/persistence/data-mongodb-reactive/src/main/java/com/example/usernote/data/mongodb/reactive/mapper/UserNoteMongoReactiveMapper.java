package com.example.usernote.data.mongodb.reactive.mapper;

import java.util.UUID;

import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveDocument;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteMongoReactiveMapper implements UserNoteMongoReactiveMapperContract {

    @Override
    public UserNoteReactiveDocument toNewDocument(UserNoteRequest request) {
        return new UserNoteReactiveDocument(UUID.randomUUID(), request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteReactiveDocument toExistingDocument(UUID id, UserNoteRequest request) {
        return new UserNoteReactiveDocument(id, request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteReactiveDocument document) {
        return new UserNoteResponse(document.getId(), document.getUserId(), document.getNoteId(), document.getRole());
    }
}
