package com.example.note.data.jpa;

import com.example.note.domain.NoteAddPort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import org.springframework.stereotype.Repository;

@Repository
class NoteAddPortAdapter implements NoteAddPort {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapper noteJpaMapper;

    NoteAddPortAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapper noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteEntity saved = noteJpaRepository.save(noteJpaMapper.toNewEntity(request));
        return noteJpaMapper.toResponse(saved);
    }

}
