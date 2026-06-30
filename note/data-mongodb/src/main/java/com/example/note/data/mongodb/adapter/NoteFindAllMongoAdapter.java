package com.example.note.data.mongodb.adapter;

import java.util.List;

import com.example.note.contract.NoteFindAllContract;
import com.example.note.data.mongodb.mapper.NoteMongoMapperContract;
import com.example.note.data.mongodb.repository.NoteMongoRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllMongoAdapter implements NoteFindAllContract {

    private final NoteMongoRepository noteMongoRepository;

    private final NoteMongoMapperContract noteMongoMapper;

    NoteFindAllMongoAdapter(NoteMongoRepository noteMongoRepository, NoteMongoMapperContract noteMongoMapper) {
        this.noteMongoRepository = noteMongoRepository;
        this.noteMongoMapper = noteMongoMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteMongoRepository.findAll().stream().map(this.noteMongoMapper::toResponse).toList();
    }

}
