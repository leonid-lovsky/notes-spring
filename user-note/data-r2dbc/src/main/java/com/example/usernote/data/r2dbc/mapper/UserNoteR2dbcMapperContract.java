package com.example.usernote.data.r2dbc.mapper;

import java.util.UUID;

import com.example.usernote.data.r2dbc.model.UserNoteR2dbcEntity;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteR2dbcMapperContract {

    UserNoteR2dbcEntity toNewEntity(UserNoteRequest request);

    UserNoteR2dbcEntity toExistingEntity(UUID id, UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteR2dbcEntity entity);

}
