package com.example.note.data.mongodb.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteContract;
import com.example.note.data.mongodb.mapper.NoteMongoMapperContract;
import com.example.note.data.mongodb.model.NoteDocument;
import com.example.note.data.mongodb.repository.NoteMongoRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
class NoteMongoAdapter implements NoteContract {

    private final MongoTemplate mongoTemplate;

    private final NoteMongoRepository noteMongoRepository;

    private final NoteMongoMapperContract noteMongoMapper;

    NoteMongoAdapter(MongoTemplate mongoTemplate, NoteMongoRepository noteMongoRepository,
            NoteMongoMapperContract noteMongoMapper) {
        this.mongoTemplate = mongoTemplate;
        this.noteMongoRepository = noteMongoRepository;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public NoteResponse add(NoteRequest request) {
        NoteDocument document = this.noteMongoMapper.toNewDocument(request);
        this.mongoTemplate.insert(document);
        return this.noteMongoMapper.toResponse(document);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.noteMongoRepository.existsById(id);
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteMongoRepository.findAll().stream().map(this.noteMongoMapper::toResponse).toList();
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteMongoRepository.findById(id).map(this.noteMongoMapper::toResponse);
    }

    @Override
    public void remove(UUID id) {
        this.noteMongoRepository.deleteById(id);
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        NoteDocument document = this.noteMongoMapper.toExistingDocument(id, request);
        this.mongoTemplate.save(document);
        return this.noteMongoMapper.toResponse(document);
    }

}
