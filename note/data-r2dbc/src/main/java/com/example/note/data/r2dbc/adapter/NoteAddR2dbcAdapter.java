package com.example.note.data.r2dbc.adapter;

import com.example.note.contract.reactive.NoteAddContractReactive;
import com.example.note.data.r2dbc.mapper.NoteR2dbcMapperContract;
import com.example.note.data.r2dbc.repository.NoteR2dbcRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
class NoteAddR2dbcAdapter implements NoteAddContractReactive {

    private final NoteR2dbcRepository noteR2dbcRepository;

    private final NoteR2dbcMapperContract noteR2dbcMapper;

    NoteAddR2dbcAdapter(NoteR2dbcRepository noteR2dbcRepository, NoteR2dbcMapperContract noteR2dbcMapper) {
        this.noteR2dbcRepository = noteR2dbcRepository;
        this.noteR2dbcMapper = noteR2dbcMapper;
    }

    @Override
    public Mono<NoteResponse> add(NoteRequest request) {
        return this.noteR2dbcRepository.save(this.noteR2dbcMapper.toNewEntity(request))
            .map(this.noteR2dbcMapper::toResponse);
    }

}
