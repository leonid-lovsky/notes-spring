package com.example.note.data.jpa.adapter;

import java.util.UUID;

import com.example.note.contract.NoteReplaceContract;
import com.example.note.data.jpa.mapper.NoteEntityMapperContract;
import com.example.note.data.jpa.model.NoteEntity;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteReplaceContractAdapter implements NoteReplaceContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteEntityMapperContract noteEntityMapper;

    NoteReplaceContractAdapter(NoteJpaRepository noteJpaRepository, NoteEntityMapperContract noteEntityMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteEntityMapper = noteEntityMapper;
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        NoteEntity saved = this.noteJpaRepository.save(this.noteEntityMapper.toExistingEntity(id, request));
        return this.noteEntityMapper.toResponse(saved);
    }

}
