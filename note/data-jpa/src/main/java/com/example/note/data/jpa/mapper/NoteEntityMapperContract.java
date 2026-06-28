package com.example.note.data.jpa.mapper;

import java.util.UUID;

import com.example.note.data.jpa.entity.NoteEntity;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

public interface NoteEntityMapperContract {

    NoteEntity toNewEntity(NoteRequest request);

    NoteEntity toExistingEntity(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteEntity entity);

}
