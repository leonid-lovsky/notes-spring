package com.example.usernote.data.jpa.mapper;

import java.util.UUID;

import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteJpaMapperContract {

    UserNoteEntity toNewEntity(UserNoteRequest request);

    UserNoteEntity toExistingEntity(UUID id, UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteEntity entity);
}
