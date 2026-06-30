package com.example.note.data.jpa.adapter;

import com.example.note.contract.NoteAddContract;
import com.example.note.data.jpa.mapper.NoteJpaMapperContract;
import com.example.note.data.jpa.model.NoteEntity;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteAddJpaAdapter implements NoteAddContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapperContract noteJpaMapper;

    NoteAddJpaAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapperContract noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toNewEntity(request));
        return this.noteJpaMapper.toResponse(saved);
    }

}
