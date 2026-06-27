package com.example.note.data.jpa;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class NoteJpaMapperImpl implements NoteJpaMapper {

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
        return new NoteResponse(entity.getId(), entity.getContent());
    }
}
