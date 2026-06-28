package com.example.note.contract.reactive;

import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;

public interface NoteFindAllContractReactive {

    Flux<NoteResponse> findAll();

}
