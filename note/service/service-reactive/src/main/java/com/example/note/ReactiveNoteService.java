package com.example.note;

import reactor.core.publisher.Mono;

import java.util.UUID;

public class ReactiveNoteService implements ReactiveFindNoteById, ReactiveCreateNoteById, ReactiveReplaceNoteById, ReactiveUpdateNoteById, ReactiveDeleteNoteById {

    private final ReactiveNoteRepository noteRepository;

    public ReactiveNoteService(ReactiveNoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Mono<NoteResponse> findNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<NoteResponse> createNoteById(UUID id, NoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<NoteResponse> replaceNoteById(UUID id, NoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<NoteResponse> updateNoteById(UUID id, NoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<NoteResponse> deleteNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
