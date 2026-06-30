package com.example.note.data.jpa.adapter;

import com.example.note.contract.NoteAddContract;
import com.example.note.data.jpa.model.NoteEntity;
import com.example.note.data.jpa.mapper.NoteEntityMapperContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteAddContractAdapter implements NoteAddContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteEntityMapperContract noteEntityMapper;

    NoteAddContractAdapter(NoteJpaRepository noteJpaRepository, NoteEntityMapperContract noteEntityMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteEntityMapper = noteEntityMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteEntity saved = this.noteJpaRepository.save(this.noteEntityMapper.toNewEntity(request));
        return this.noteEntityMapper.toResponse(saved);
    }

}
