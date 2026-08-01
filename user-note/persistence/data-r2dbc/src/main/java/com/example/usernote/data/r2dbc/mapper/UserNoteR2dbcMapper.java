package com.example.usernote.data.r2dbc.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.usernote.data.r2dbc.model.UserNoteR2dbcEntity;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteR2dbcMapper implements UserNoteR2dbcMapperContract {

    @Override
    public UserNoteR2dbcEntity toNewEntity(UserNoteRequest request) {
        return new UserNoteR2dbcEntity(request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteR2dbcEntity toExistingEntity(UUID id, UserNoteRequest request) {
        return new UserNoteR2dbcEntity(id, request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteR2dbcEntity entity) {
        return new UserNoteResponse(Objects.requireNonNull(entity.getId()), entity.getUserId(), entity.getNoteId(),
                entity.getRole());
    }
}
