package com.example.usernote.data.jpa.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

import org.springframework.stereotype.Component;

@Component
class UserNoteJpaMapper implements UserNoteJpaMapperContract {

    @Override
    public UserNoteEntity toNewEntity(UserNoteRequest request) {
        return new UserNoteEntity(request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteEntity toExistingEntity(UUID id, UserNoteRequest request) {
        return new UserNoteEntity(id, request.userId(), request.noteId(), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteEntity entity) {
        return new UserNoteResponse(Objects.requireNonNull(entity.getId()), entity.getUserId(), entity.getNoteId(),
                entity.getRole());
    }
}
