package com.example.note.contract.reactive;

import java.util.UUID;

import com.example.note.domain.NoteResponse;

import reactor.core.publisher.Mono;

public interface NoteFindByIdContractReactive {

    Mono<NoteResponse> findById(UUID id);

}
