package com.example.note.data.mongodb.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteFindByIdContract;
import com.example.note.data.mongodb.mapper.NoteMongoMapperContract;
import com.example.note.data.mongodb.repository.NoteMongoRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdMongoAdapter implements NoteFindByIdContract {

    private final NoteMongoRepository noteMongoRepository;

    private final NoteMongoMapperContract noteMongoMapper;

    NoteFindByIdMongoAdapter(NoteMongoRepository noteMongoRepository, NoteMongoMapperContract noteMongoMapper) {
        this.noteMongoRepository = noteMongoRepository;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteMongoRepository.findById(id).map(this.noteMongoMapper::toResponse);
    }

}
