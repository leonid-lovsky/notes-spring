package com.example.note.data.jdbc.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.note.data.jdbc.model.NoteJdbcEntity;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Component;

@Component
class NoteJdbcMapper implements NoteJdbcMapperContract {

    @Override
    public NoteJdbcEntity toNewEntity(NoteRequest request) {
        return new NoteJdbcEntity(request.content());
    }

    @Override
    public NoteJdbcEntity toExistingEntity(UUID id, NoteRequest request) {
        return new NoteJdbcEntity(id, request.content());
    }

    @Override
    public NoteResponse toResponse(NoteJdbcEntity entity) {
        return new NoteResponse(Objects.requireNonNull(entity.getId()), entity.getContent());
    }
}
