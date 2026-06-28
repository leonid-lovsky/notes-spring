package com.example.note.data.mongodb.adapter;

import java.util.List;

import com.example.note.contract.NoteFindAllContract;
import com.example.note.data.mongodb.mapper.NoteDocumentMapperContract;
import com.example.note.data.mongodb.repository.NoteMongoRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllContractAdapter implements NoteFindAllContract {

    private final NoteMongoRepository noteMongoRepository;

    private final NoteDocumentMapperContract noteDocumentMapper;

    NoteFindAllContractAdapter(NoteMongoRepository noteMongoRepository, NoteDocumentMapperContract noteDocumentMapper) {
        this.noteMongoRepository = noteMongoRepository;
        this.noteDocumentMapper = noteDocumentMapper;
    }

    @Override
    public List<NoteResponse> findAll() {
        return this.noteMongoRepository.findAll().stream().map(this.noteDocumentMapper::toResponse).toList();
    }

}
