package com.example.note.data.r2dbc.mapper;

import java.util.Objects;
import java.util.UUID;

import com.example.note.data.r2dbc.entity.NoteR2dbcEntity;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Component;

@Component
class NoteR2dbcMapper implements NoteR2dbcMapperContract {

    @Override
    public NoteR2dbcEntity toNewEntity(NoteRequest request) {
        return new NoteR2dbcEntity(request.content());
    }

    @Override
    public NoteR2dbcEntity toExistingEntity(UUID id, NoteRequest request) {
        return new NoteR2dbcEntity(id, request.content());
    }

    @Override
    public NoteResponse toResponse(NoteR2dbcEntity entity) {
        return new NoteResponse(Objects.requireNonNull(entity.getId()), entity.getContent());
    }

}
