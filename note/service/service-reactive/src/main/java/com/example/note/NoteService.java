package com.example.note;

import java.util.UUID;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NoteService implements ReactiveFindNoteById, ReactiveCreateNoteById, ReactiveReplaceNoteById, ReactiveUpdateNoteById, ReactiveDeleteNoteById {

    private final ReactiveCrudRepository<Note, UUID> noteCrudRepository;
    private final ReactiveSortingRepository<Note, UUID> noteSortingRepository;
    private final ReactiveQueryByExampleExecutor<Note> noteQueryByExampleExecutor;

    public NoteService(
        ReactiveCrudRepository<Note, UUID> noteCrudRepository,
        ReactiveSortingRepository<Note, UUID> noteSortingRepository,
        ReactiveQueryByExampleExecutor<Note> noteQueryByExampleExecutor
    ) {
        this.noteCrudRepository = noteCrudRepository;
        this.noteSortingRepository = noteSortingRepository;
        this.noteQueryByExampleExecutor = noteQueryByExampleExecutor;
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
