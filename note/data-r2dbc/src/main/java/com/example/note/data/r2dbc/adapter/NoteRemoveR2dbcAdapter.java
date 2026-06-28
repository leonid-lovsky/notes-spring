package com.example.note.data.r2dbc.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteRemoveContractReactive;
import com.example.note.data.r2dbc.repository.NoteR2dbcRepository;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
class NoteRemoveR2dbcAdapter implements NoteRemoveContractReactive {

    private final NoteR2dbcRepository noteR2dbcRepository;

    NoteRemoveR2dbcAdapter(NoteR2dbcRepository noteR2dbcRepository) {
        this.noteR2dbcRepository = noteR2dbcRepository;
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.noteR2dbcRepository.deleteById(id);
    }

}
