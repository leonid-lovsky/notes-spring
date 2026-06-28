package com.example.note.data.mongodb;

import java.util.List;

import com.example.note.domain.NoteFindAllPort;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllPortAdapter implements NoteFindAllPort {

    private final NoteMongoRepository noteMongoRepository;

    private final NoteMongoMapper noteMongoMapper;

    NoteFindAllPortAdapter(NoteMongoRepository noteMongoRepository, NoteMongoMapper noteMongoMapper) {
        this.noteMongoRepository = noteMongoRepository;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteMongoRepository.findAll().stream().map(this.noteMongoMapper::toResponse).toList();
    }

}
