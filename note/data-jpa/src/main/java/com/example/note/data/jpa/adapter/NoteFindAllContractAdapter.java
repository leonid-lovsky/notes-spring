package com.example.note.data.jpa.adapter;

import java.util.List;

import com.example.note.contract.NoteFindAllContract;
import com.example.note.data.jpa.mapper.NoteEntityMapperContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllContractAdapter implements NoteFindAllContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteEntityMapperContract noteEntityMapper;

    NoteFindAllContractAdapter(NoteJpaRepository noteJpaRepository, NoteEntityMapperContract noteEntityMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteEntityMapper = noteEntityMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteJpaRepository.findAll().stream().map(this.noteEntityMapper::toResponse).toList();
    }

}
