package com.example.note.data.jdbc.mapper;

import java.util.UUID;

import com.example.note.data.jdbc.model.NoteJdbcEntity;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteJdbcMapperContract {

    NoteJdbcEntity toNewEntity(NoteRequest request);

    NoteJdbcEntity toExistingEntity(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteJdbcEntity entity);
}
