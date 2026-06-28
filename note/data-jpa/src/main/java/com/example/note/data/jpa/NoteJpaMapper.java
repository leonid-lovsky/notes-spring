package com.example.note.data.jpa;

import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

interface NoteJpaMapper {

    NoteEntity toNewEntity(NoteRequest request);

    NoteEntity toExistingEntity(UUID id, NoteRequest request);

    NoteResponse toResponse(NoteEntity entity);

}
