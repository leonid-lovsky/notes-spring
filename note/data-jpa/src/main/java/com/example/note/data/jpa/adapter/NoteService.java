package com.example.note.data.jpa.adapter;

import java.util.List;
import java.util.UUID;

import com.example.note.contract.NoteServiceInterface;
import com.example.note.data.jpa.mapper.NoteJpaMapperContract;
import com.example.note.data.jpa.model.NoteEntity;
import com.example.note.data.jpa.repository.NoteJpaRepository;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteService implements NoteServiceInterface {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapperContract noteJpaMapper;

    NoteService(NoteJpaRepository noteJpaRepository, NoteJpaMapperContract noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public Boolean existsById(UUID id) {
        return this.noteJpaRepository.existsById(id);
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toNewEntity(request));
        return this.noteJpaMapper.toResponse(saved);
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteJpaRepository.findAll().stream().map(this.noteJpaMapper::toResponse).toList();
    }

    @Override
    public NoteResponse findById(UUID id) {
        return this.noteJpaRepository.findById(id)
            .map(this.noteJpaMapper::toResponse)
            .orElseThrow(() -> new NoteNotFoundException(id));
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        if (!this.noteJpaRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toExistingEntity(id, request));
        return this.noteJpaMapper.toResponse(saved);
    }

    @Override
    public NoteResponse merge(UUID id, NoteRequest request) {
        NoteEntity existing = this.noteJpaRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        NoteRequest merged = merge(this.noteJpaMapper.toResponse(existing), request);
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toExistingEntity(id, merged));
        return this.noteJpaMapper.toResponse(saved);
    }

    @Override
    public NoteResponse remove(UUID id) {
        NoteEntity existing = this.noteJpaRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        this.noteJpaRepository.deleteById(id);
        return this.noteJpaMapper.toResponse(existing);
    }

    private static NoteRequest merge(NoteResponse existing, NoteRequest request) {
        String content = (request.content() != null) ? request.content() : existing.content();
        return new NoteRequest(content);
    }
}
