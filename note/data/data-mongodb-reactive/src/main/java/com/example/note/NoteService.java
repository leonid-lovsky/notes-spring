package com.example.note;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NoteService implements FindNoteById, CreateNoteById, ReplaceNoteById, UpdateNoteById, DeleteNoteById {

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
