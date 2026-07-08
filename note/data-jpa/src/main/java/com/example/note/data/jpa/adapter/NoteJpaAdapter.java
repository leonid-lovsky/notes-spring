package com.example.note.data.jpa.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteContract;
import com.example.note.data.jpa.mapper.NoteJpaMapperContract;
import com.example.note.data.jpa.model.NoteEntity;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteJpaAdapter implements NoteContract {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapperContract noteJpaMapper;

    NoteJpaAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapperContract noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toNewEntity(request));
        return this.noteJpaMapper.toResponse(saved);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.noteJpaRepository.existsById(id);
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteJpaRepository.findAll().stream().map(this.noteJpaMapper::toResponse).toList();
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteJpaRepository.findById(id).map(this.noteJpaMapper::toResponse);
    }

    @Override
    public void remove(UUID id) {
        this.noteJpaRepository.deleteById(id);
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toExistingEntity(id, request));
        return this.noteJpaMapper.toResponse(saved);
    }

}
