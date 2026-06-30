package com.example.note.data.jpa.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.note.data.jpa.model.NoteEntity;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Component;

@Component
class NoteEntityMapper implements NoteEntityMapperContract {

    @Override
    public NoteEntity toNewEntity(NoteRequest request) {
        return new NoteEntity(request.content());
    }

    @Override
    public NoteEntity toExistingEntity(UUID id, NoteRequest request) {
        return new NoteEntity(id, request.content());
    }

    @Override
    public NoteResponse toResponse(NoteEntity entity) {
        return new NoteResponse(Objects.requireNonNull(entity.getId()), entity.getContent());
    }

}
