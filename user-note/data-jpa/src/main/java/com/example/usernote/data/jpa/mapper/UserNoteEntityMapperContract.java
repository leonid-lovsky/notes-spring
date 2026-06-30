package com.example.usernote.data.jpa.mapper;

import com.example.usernote.data.jpa.model.UserNoteEntity;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

public interface UserNoteEntityMapperContract {

    UserNoteEntity toEntity(UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteEntity entity);

}
