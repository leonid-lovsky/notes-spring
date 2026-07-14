package com.example.usernote.data.jdbc.mapper;

import java.util.UUID;

import com.example.usernote.data.jdbc.model.UserNoteJdbcEntity;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteJdbcMapperContract {

    UserNoteJdbcEntity toNewEntity(UserNoteRequest request);

    UserNoteJdbcEntity toExistingEntity(UUID id, UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteJdbcEntity entity);
}
