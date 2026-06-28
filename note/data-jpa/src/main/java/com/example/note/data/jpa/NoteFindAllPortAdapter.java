package com.example.note.data.jpa;

import java.util.List;

import com.example.note.domain.NoteFindAllPort;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllPortAdapter implements NoteFindAllPort {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapper noteJpaMapper;

    NoteFindAllPortAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapper noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteJpaRepository.findAll().stream().map(this.noteJpaMapper::toResponse).toList();
    }

}
