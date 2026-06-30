package com.example.note.data.jpa.adapter;

import java.util.List;

import com.example.note.contract.NoteFindAllContract;
import com.example.note.data.jpa.mapper.NoteJpaMapperContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllJpaAdapter implements NoteFindAllContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapperContract noteJpaMapper;

    NoteFindAllJpaAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapperContract noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteJpaRepository.findAll().stream().map(this.noteJpaMapper::toResponse).toList();
    }

}
