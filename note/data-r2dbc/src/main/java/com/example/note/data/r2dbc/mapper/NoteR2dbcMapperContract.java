package com.example.note.data.r2dbc.mapper;

import java.util.UUID;

import com.example.note.data.r2dbc.model.NoteR2dbcEntity;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteR2dbcMapperContract {

    NoteR2dbcEntity toNewEntity(NoteRequest request);

    NoteR2dbcEntity toExistingEntity(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteR2dbcEntity entity);
}
