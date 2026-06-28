package com.example.note.data.jpa.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteFindByIdContract;
import com.example.note.data.jpa.mapper.NoteEntityMapperContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdContractAdapter implements NoteFindByIdContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteEntityMapperContract noteEntityMapper;

    NoteFindByIdContractAdapter(NoteJpaRepository noteJpaRepository, NoteEntityMapperContract noteEntityMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteEntityMapper = noteEntityMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteJpaRepository.findById(id).map(this.noteEntityMapper::toResponse);
    }

}
