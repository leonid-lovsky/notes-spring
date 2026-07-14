package com.example.note.data.mongodb.adapter;

import java.util.List;
import java.util.UUID;

import com.example.note.contract.NoteServiceInterface;
import com.example.note.data.mongodb.mapper.NoteMongoMapperContract;
import com.example.note.data.mongodb.model.NoteDocument;
import com.example.note.data.mongodb.repository.NoteMongoRepository;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
class NoteService implements NoteServiceInterface {

    private final MongoTemplate mongoTemplate;

    private final NoteMongoRepository noteMongoRepository;

    private final NoteMongoMapperContract noteMongoMapper;

    NoteService(MongoTemplate mongoTemplate, NoteMongoRepository noteMongoRepository,
            NoteMongoMapperContract noteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.noteMongoRepository = noteMongoRepository;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public Boolean existsById(UUID id) {
        return this.noteMongoRepository.existsById(id);
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteDocument document = this.noteMongoMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.noteMongoMapper.toResponse(document);
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteMongoRepository.findAll().stream().map(this.noteMongoMapper::toResponse).toList();
    }

    @Override
    public NoteResponse findById(UUID id) {
        return this.noteMongoRepository.findById(id)
            .map(this.noteMongoMapper::toResponse)
            .orElseThrow(() -> new NoteNotFoundException(id));
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        if (!this.noteMongoRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        NoteDocument document = this.noteMongoMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.noteMongoMapper.toResponse(document);
    }

    @Override
    public NoteResponse merge(UUID id, NoteRequest request) {
        NoteDocument existing = this.noteMongoRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        NoteRequest merged = merge(this.noteMongoMapper.toResponse(existing), request);
        NoteDocument document = this.noteMongoMapper.toExistingDocument(id, merged);
        this.mongoTemplate.save(document);
        return this.noteMongoMapper.toResponse(document);
    }

    @Override
    public NoteResponse remove(UUID id) {
        NoteDocument existing = this.noteMongoRepository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
        this.noteMongoRepository.deleteById(id);
        return this.noteMongoMapper.toResponse(existing);
    }

    private static NoteRequest merge(NoteResponse existing, NoteRequest request) {
        String content = (request.content() != null) ? request.content() : existing.content();
        return new NoteRequest(content);
    }
}
