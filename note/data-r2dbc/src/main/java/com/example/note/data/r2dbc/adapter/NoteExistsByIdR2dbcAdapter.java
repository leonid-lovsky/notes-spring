package com.example.note.data.r2dbc.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteExistsByIdContractReactive;
import com.example.note.data.r2dbc.repository.NoteR2dbcRepository;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
class NoteExistsByIdR2dbcAdapter implements NoteExistsByIdContractReactive {

    private final NoteR2dbcRepository noteR2dbcRepository;

    NoteExistsByIdR2dbcAdapter(NoteR2dbcRepository noteR2dbcRepository) {
        this.noteR2dbcRepository = noteR2dbcRepository;
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.noteR2dbcRepository.existsById(id);
    }

}
