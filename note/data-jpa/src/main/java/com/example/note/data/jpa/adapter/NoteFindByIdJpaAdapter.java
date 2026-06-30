package com.example.note.data.jpa.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteFindByIdContract;
import com.example.note.data.jpa.mapper.NoteJpaMapperContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdJpaAdapter implements NoteFindByIdContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapperContract noteJpaMapper;

    NoteFindByIdJpaAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapperContract noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteJpaRepository.findById(id).map(this.noteJpaMapper::toResponse);
    }

}
