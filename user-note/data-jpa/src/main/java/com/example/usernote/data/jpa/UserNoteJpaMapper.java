package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;

interface UserNoteJpaMapper {

    UserNoteEntity toEntity(UserNoteRequest request);

    UserNoteResponse toResponse(UserNoteEntity entity);

}
