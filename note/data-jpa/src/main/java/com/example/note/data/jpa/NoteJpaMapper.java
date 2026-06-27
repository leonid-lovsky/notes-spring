package com.example.note.data.jpa;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import java.util.UUID;

interface NoteJpaMapper {

	NoteEntity toNewEntity(NoteRequest request);

	NoteEntity toExistingEntity(UUID id, NoteRequest request);

	NoteResponse toResponse(NoteEntity entity);

}
