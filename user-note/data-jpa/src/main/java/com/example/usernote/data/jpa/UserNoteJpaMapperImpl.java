package com.example.usernote.data.jpa;

import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import org.springframework.stereotype.Component;

@Component
class UserNoteJpaMapperImpl implements UserNoteJpaMapper {

    @Override
    public UserNoteEntity toEntity(UserNoteRequest request) {
        return new UserNoteEntity(new UserNoteId(request.userId(), request.noteId()), request.role());
    }

    @Override
    public UserNoteResponse toResponse(UserNoteEntity entity) {
        return new UserNoteResponse(entity.getId().getUserId(), entity.getId().getNoteId(), entity.getRole());
    }

}
