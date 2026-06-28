package com.example.usernote.data.jpa.mapper;

import com.example.usernote.data.jpa.entity.UserNoteEntity;
import com.example.usernote.data.jpa.entity.UserNoteId;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteEntityMapper implements UserNoteEntityMapperContract {

    @Override
    public UserNoteEntity toEntity(UserNoteRequest request) {
        return new UserNoteEntity(new UserNoteId(request.userId(), request.noteId()), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteEntity entity) {
        return new UserNoteResponse(entity.getId().getUserId(), entity.getId().getNoteId(), entity.getRole());
    }

}
