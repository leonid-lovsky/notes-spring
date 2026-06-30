package com.example.usernote.data.mongodb.mapper;

import com.example.usernote.data.mongodb.model.UserNoteDocument;
import com.example.usernote.data.mongodb.model.UserNoteKey;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteMongoMapper implements UserNoteMongoMapperContract {

    @Override
    public UserNoteDocument toDocument(UserNoteRequest request) {
        return new UserNoteDocument(new UserNoteKey(request.userId(), request.noteId()), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteDocument document) {
        return new UserNoteResponse(document.getId().getUserId(), document.getId().getNoteId(), document.getRole());
    }

}
