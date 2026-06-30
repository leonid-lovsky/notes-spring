package com.example.usernote.data.mongodb.reactive.mapper;

import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveDocument;
import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveKey;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteReactiveDocumentMapper implements UserNoteReactiveDocumentMapperContract {

    @Override
    public UserNoteReactiveDocument toDocument(UserNoteRequest request) {
        return new UserNoteReactiveDocument(new UserNoteReactiveKey(request.userId(), request.noteId()),
                request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteReactiveDocument document) {
        return new UserNoteResponse(document.getId().getUserId(), document.getId().getNoteId(), document.getRole());
    }

}
