package com.example.note.data.jpa.adapter;

import java.util.UUID;

import com.example.note.contract.NoteReplaceContract;
import com.example.note.data.jpa.mapper.NoteJpaMapperContract;
import com.example.note.data.jpa.model.NoteEntity;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteReplaceJpaAdapter implements NoteReplaceContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapperContract noteJpaMapper;

    NoteReplaceJpaAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapperContract noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toExistingEntity(id, request));
        return this.noteJpaMapper.toResponse(saved);
    }

}
