package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.stereotype.Component;

@Component
class UserNoteMongoMapperImpl implements UserNoteMongoMapper {

    @Override
    public UserNoteDocument toDocument(UserNoteRequest request) {
        return new UserNoteDocument(new UserNoteKey(request.userId(), request.noteId()), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteDocument document) {
        return new UserNoteResponse(document.getId().getUserId(), document.getId().getNoteId(), document.getRole());
    }

}
