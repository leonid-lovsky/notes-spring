package com.example.usernote.data.jdbc.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.usernote.data.jdbc.model.UserNoteJdbcEntity;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteJdbcMapper implements UserNoteJdbcMapperContract {

    @Override
    public UserNoteJdbcEntity toNewEntity(UserNoteRequest request) {
        return new UserNoteJdbcEntity(request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteJdbcEntity toExistingEntity(UUID id, UserNoteRequest request) {
        return new UserNoteJdbcEntity(id, request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteJdbcEntity entity) {
        return new UserNoteResponse(Objects.requireNonNull(entity.getId()), entity.getUserId(), entity.getNoteId(),
                entity.getRole());
    }
}
