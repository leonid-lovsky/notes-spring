package com.example.note.contract.reactive;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

public interface NoteAddContractReactive {

    Mono<NoteResponse> add(NoteRequest request);

}
