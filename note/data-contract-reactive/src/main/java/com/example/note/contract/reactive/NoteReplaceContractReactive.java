package com.example.note.contract.reactive;

import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

public interface NoteReplaceContractReactive {

    Mono<NoteResponse> replace(UUID id, NoteRequest request);

}
