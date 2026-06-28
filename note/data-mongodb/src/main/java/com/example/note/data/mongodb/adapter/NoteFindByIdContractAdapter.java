package com.example.note.data.mongodb.adapter;

import java.util.Optional;
import java.util.UUID;

import com.example.note.contract.NoteFindByIdContract;
import com.example.note.data.mongodb.mapper.NoteDocumentMapperContract;
import com.example.note.data.mongodb.repository.NoteMongoRepository;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdContractAdapter implements NoteFindByIdContract {

    private final NoteMongoRepository noteMongoRepository;

    private final NoteDocumentMapperContract noteDocumentMapper;

    NoteFindByIdContractAdapter(NoteMongoRepository noteMongoRepository,
            NoteDocumentMapperContract noteDocumentMapper) {
        this.noteMongoRepository = noteMongoRepository;
        this.noteDocumentMapper = noteDocumentMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteMongoRepository.findById(id).map(this.noteDocumentMapper::toResponse);
    }

}
