package com.example.note.data.r2dbc.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteFindByIdContractReactive;
import com.example.note.data.r2dbc.mapper.NoteR2dbcMapperContract;
import com.example.note.data.r2dbc.repository.NoteR2dbcRepository;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdR2dbcAdapter implements NoteFindByIdContractReactive {

    private final NoteR2dbcRepository noteR2dbcRepository;

    private final NoteR2dbcMapperContract noteR2dbcMapper;

    NoteFindByIdR2dbcAdapter(NoteR2dbcRepository noteR2dbcRepository, NoteR2dbcMapperContract noteR2dbcMapper) {
        this.noteR2dbcRepository = noteR2dbcRepository;
        this.noteR2dbcMapper = noteR2dbcMapper;
    }

    @Override
    public Mono<NoteResponse> findById(UUID id) {
        return this.noteR2dbcRepository.findById(id).map(this.noteR2dbcMapper::toResponse);
    }

}
