package com.example.note.data.r2dbc.adapter;

import com.example.note.contract.reactive.NoteFindAllContractReactive;
import com.example.note.data.r2dbc.mapper.NoteR2dbcMapperContract;
import com.example.note.data.r2dbc.repository.NoteR2dbcRepository;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllR2dbcAdapter implements NoteFindAllContractReactive {

    private final NoteR2dbcRepository noteR2dbcRepository;

    private final NoteR2dbcMapperContract noteR2dbcMapper;

    NoteFindAllR2dbcAdapter(NoteR2dbcRepository noteR2dbcRepository, NoteR2dbcMapperContract noteR2dbcMapper) {
        this.noteR2dbcRepository = noteR2dbcRepository;
        this.noteR2dbcMapper = noteR2dbcMapper;
    }

    @Override
    public Flux<NoteResponse> findAll() {
        return this.noteR2dbcRepository.findAll().map(this.noteR2dbcMapper::toResponse);
    }

}
