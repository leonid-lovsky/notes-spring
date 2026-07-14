package com.example.note.data.jdbc.adapter;

import java.util.List;
import java.util.UUID;

import com.example.note.contract.NoteServiceInterface;
import com.example.note.data.jdbc.mapper.NoteJdbcMapperContract;
import com.example.note.data.jdbc.model.NoteJdbcEntity;
import com.example.note.data.jdbc.repository.NoteJdbcRepository;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteService implements NoteServiceInterface {

    private final NoteJdbcRepository noteJdbcRepository;

    private final NoteJdbcMapperContract noteJdbcMapper;

    NoteService(NoteJdbcRepository noteJdbcRepository, NoteJdbcMapperContract noteJdbcMapper) {
        this.noteJdbcRepository = noteJdbcRepository;
        this.noteJdbcMapper = noteJdbcMapper;
    }

    @Override
    public Boolean existsById(UUID id) {
        return this.noteJdbcRepository.existsById(id);
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteJdbcEntity saved = this.noteJdbcRepository.save(this.noteJdbcMapper.toNewEntity(request));
        return this.noteJdbcMapper.toResponse(saved);
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteJdbcRepository.findAll().stream().map(this.noteJdbcMapper::toResponse).toList();
    }

    @Override
    public NoteResponse findById(UUID id) {
        return this.noteJdbcRepository.findById(id)
            .map(this.noteJdbcMapper::toResponse)
            .orElseThrow(() -> new NoteNotFoundException(id));
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        if (!this.noteJdbcRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        NoteJdbcEntity saved = this.noteJdbcRepository.save(this.noteJdbcMapper.toExistingEntity(id, request));
        return this.noteJdbcMapper.toResponse(saved);
    }

    @Override
    public NoteResponse merge(UUID id, NoteRequest request) {
        NoteJdbcEntity existing = this.noteJdbcRepository.findById(id)
            .orElseThrow(() -> new NoteNotFoundException(id));
        NoteRequest merged = merge(this.noteJdbcMapper.toResponse(existing), request);
        NoteJdbcEntity saved = this.noteJdbcRepository.save(this.noteJdbcMapper.toExistingEntity(id, merged));
        return this.noteJdbcMapper.toResponse(saved);
    }

    @Override
    public NoteResponse remove(UUID id) {
        NoteJdbcEntity existing = this.noteJdbcRepository.findById(id)
            .orElseThrow(() -> new NoteNotFoundException(id));
        this.noteJdbcRepository.deleteById(id);
        return this.noteJdbcMapper.toResponse(existing);
    }

    private static NoteRequest merge(NoteResponse existing, NoteRequest request) {
        String content = (request.content() != null) ? request.content() : existing.content();
        return new NoteRequest(content);
    }
}
