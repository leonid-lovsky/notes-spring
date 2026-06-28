package com.example.note.data.mongodb;

import java.util.Optional;
import java.util.UUID;

import com.example.note.domain.NoteFindByIdPort;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdPortAdapter implements NoteFindByIdPort {

    private final NoteMongoRepository noteMongoRepository;

    private final NoteMongoMapper noteMongoMapper;

    NoteFindByIdPortAdapter(NoteMongoRepository noteMongoRepository, NoteMongoMapper noteMongoMapper) {
        this.noteMongoRepository = noteMongoRepository;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteMongoRepository.findById(id).map(this.noteMongoMapper::toResponse);
    }

}
